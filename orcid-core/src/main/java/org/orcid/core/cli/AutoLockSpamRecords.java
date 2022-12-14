package org.orcid.core.cli;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.orcid.core.admin.LockReason;
import org.orcid.core.manager.ProfileEntityCacheManager;
import org.orcid.core.manager.v3.NotificationManager;
import org.orcid.core.manager.v3.ProfileEntityManager;
import org.orcid.core.togglz.OrcidTogglzConfiguration;
import org.orcid.pojo.ajaxForm.PojoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.togglz.core.context.ContextClassLoaderFeatureManagerProvider;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.manager.FeatureManagerBuilder;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class AutoLockSpamRecords {

    private static final Logger LOG = LoggerFactory.getLogger(AutoLockSpamRecords.class);

    @Option(name = "-b", usage = "Maximum number of records to be locked in a day")
    private int dailyBatchSize;

    @Option(name = "-f", usage = "The location of csv file that contains spam records to lock")
    private String spamCsvFile;
    
    @Resource(name = "notificationManagerV3")
    private NotificationManager notificationManager;

    private static int ONE_DAY = 86400000;

    private ProfileEntityManager profileEntityManager;
    
    @Resource
    private ProfileEntityCacheManager profileEntityCacheManager;

    public static void main(String[] args) {
        AutoLockSpamRecords autolockSpamRecords = new AutoLockSpamRecords();
        CmdLineParser parser = new CmdLineParser(autolockSpamRecords);
        List<String> allIDs;
        try {
            parser.parseArgument(args);
            autolockSpamRecords.validateParameters(parser);
            autolockSpamRecords.init();

            allIDs = autolockSpamRecords.getAllSpamIDs();
            System.out.println("Found " + allIDs.size() + " profiles for autolocking");
            LOG.info("Found {} profiles for autolocking", allIDs.size());

            List<String> toLock = autolockSpamRecords.getNextIdSubset(allIDs);
            while (toLock != null && !toLock.isEmpty()) {
                autolockSpamRecords.autolockRecords(toLock);
                LOG.info("Locked {} profiles, {} remaining to lock", new Object[] { toLock.size(), allIDs.size() });
                LOG.info("Profiles autolocked");
                Thread.sleep(ONE_DAY);
                toLock = autolockSpamRecords.getNextIdSubset(allIDs);
            }
        } catch (Exception e) {
            LOG.error("Exception when locking spam records", e);
            System.err.println(e.getMessage());
        } finally {
            System.exit(0);
        }

    }

    private void autolockRecords(List<String> toLock) {
        String lastOrcidProcessed = "";
        System.out.println("Start for batch: " + System.currentTimeMillis() + " to lock batch is: " + toLock.size());
        for (String orcidId : toLock) {
            try {
                System.out.println("Processing orcidId: " + orcidId);
                profileEntityManager.findByOrcid(orcidId);
                //check if is not already locked
                profileEntityManager.lockProfile(orcidId, LockReason.SPAM_AUTO.getLabel(), "ML Detected", "");
                
                lastOrcidProcessed = orcidId;
            } catch (Exception e) {
                LOG.error("Exception when locking spam record " + orcidId, e);
                e.printStackTrace();
            }
        }
        System.out.println("Spam locking for the batch processed on the day: " + System.currentTimeMillis() + " lastOrcid processed is: " + lastOrcidProcessed);
        LOG.info("Spam locking for the batch processed on the day: " + System.currentTimeMillis() + " lastOrcid processed is: " + lastOrcidProcessed);

    }

    @SuppressWarnings("resource")
    private void init() {
        ApplicationContext context = new ClassPathXmlApplicationContext("orcid-core-context.xml");
        profileEntityManager = (ProfileEntityManager) context.getBean("profileEntityManagerV3");
        profileEntityCacheManager = (ProfileEntityCacheManager) context.getBean("profileEntityCacheManager");
        notificationManager = (NotificationManager) context.getBean("notificationManagerV3");
        bootstrapTogglz(context.getBean(OrcidTogglzConfiguration.class));
    }

    private List<String> getNextIdSubset(List<String> ids) {
        List<String> subset = new ArrayList<>();
        for (int i = 0; i < dailyBatchSize && !ids.isEmpty(); i++) {
            subset.add(ids.remove(0));
        }
        return subset;
    }

    private ArrayList<String> getAllSpamIDs() throws IOException {
        FileReader reader = new FileReader(spamCsvFile);

        Iterator<Map<String, String>> iterator = new CsvMapper().readerFor(Map.class)
                .with(CsvSchema.emptySchema().withHeader().withColumnSeparator(',').withoutQuoteChar()).readValues(reader);
        ArrayList<String> spamList = new ArrayList<String>();
        Map<String, String> keyVals = null;
        while (iterator.hasNext()) {
            keyVals = iterator.next();
            Object[] keys = keyVals.keySet().toArray();
            spamList.add(keyVals.get(keys[5]));
        }
        return spamList;
    }

    public void validateParameters(CmdLineParser parser) throws CmdLineException {
        if (dailyBatchSize <= 0) {
            throw new CmdLineException(parser, "-b parameter must not be null. A positive integer is expected.");
        }

        if (PojoUtil.isEmpty(spamCsvFile)) {
            throw new CmdLineException(parser, "-f parameter must not be null. Please specify the location of csv file");
        }
    }
    
    private static void bootstrapTogglz(OrcidTogglzConfiguration togglzConfig) {
        FeatureManager featureManager = new FeatureManagerBuilder().togglzConfig(togglzConfig).build();
        ContextClassLoaderFeatureManagerProvider.bind(featureManager);  
    }

}
