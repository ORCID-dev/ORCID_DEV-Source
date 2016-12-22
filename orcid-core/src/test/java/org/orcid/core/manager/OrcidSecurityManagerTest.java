/**
 * =============================================================================
 *
 * ORCID (R) Open Source
 * http://orcid.org
 *
 * Copyright (c) 2012-2014 ORCID, Inc.
 * Licensed under an MIT-Style License (MIT)
 * http://orcid.org/open-source-license
 *
 * This copyright and license information (including a link to the full license)
 * shall be included in its entirety in all copies or substantial portion of
 * the software.
 *
 * =============================================================================
 */
package org.orcid.core.manager;

import static org.junit.Assert.fail;

import java.security.AccessControlException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.orcid.core.exception.OrcidUnauthorizedException;
import org.orcid.core.exception.OrcidVisibilityException;
import org.orcid.core.utils.SecurityContextTestUtils;
import org.orcid.jaxb.model.common_rc4.CreditName;
import org.orcid.jaxb.model.common_rc4.Source;
import org.orcid.jaxb.model.common_rc4.SourceClientId;
import org.orcid.jaxb.model.common_rc4.Visibility;
import org.orcid.jaxb.model.message.ScopePathType;
import org.orcid.jaxb.model.record_rc4.Biography;
import org.orcid.jaxb.model.record_rc4.Name;
import org.orcid.jaxb.model.record_rc4.OtherName;
import org.orcid.jaxb.model.record_rc4.Work;
import org.orcid.persistence.jpa.entities.ClientDetailsEntity;
import org.orcid.persistence.jpa.entities.ProfileEntity;
import org.orcid.persistence.jpa.entities.SourceEntity;
import org.orcid.test.OrcidJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;

/**
 * 
 * @author Will Simpson
 *
 */
@RunWith(OrcidJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:orcid-core-context.xml" })
public class OrcidSecurityManagerTest {

	@Resource
	private OrcidSecurityManager orcidSecurityManager;

	private final String ORCID_1 = "0000-0000-0000-0001";
	private final String ORCID_2 = "0000-0000-0000-0002";

	private final String CLIENT_1 = "APP-0000000000000001";
	private final String CLIENT_2 = "APP-0000000000000002";

	@Before
	public void before() {

	}

	@After
	public void after() {
		SecurityContextTestUtils.setUpSecurityContextForAnonymous();
	}

	/**
	 * =================== checkScopes test's ===================
	 */
	@Test
	public void testCheckScopes_ReadPublic() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.READ_PUBLIC);
		assertCheckScopesFailForOtherScopes(ScopePathType.READ_PUBLIC);
	}

	@Test
	public void testCheckScopes_Authenticate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.AUTHENTICATE);
		assertCheckScopesFailForOtherScopes(ScopePathType.AUTHENTICATE, ScopePathType.READ_PUBLIC);
	}

	@Test
	public void testCheckScopes_AffiliationsReadLimited() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.AFFILIATIONS_READ_LIMITED);
		assertCheckScopesFailForOtherScopes(ScopePathType.READ_PUBLIC, ScopePathType.AFFILIATIONS_READ_LIMITED);
	}

	@Test
	public void testCheckScopes_AffiliationsCreate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.AFFILIATIONS_CREATE);
		assertCheckScopesFailForOtherScopes(ScopePathType.READ_PUBLIC, ScopePathType.AFFILIATIONS_CREATE);
	}

	@Test
	public void testCheckScopes_AffiliationsUpdate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.AFFILIATIONS_UPDATE);
		assertCheckScopesFailForOtherScopes(ScopePathType.READ_PUBLIC, ScopePathType.AFFILIATIONS_UPDATE);
	}

	@Test
	public void testCheckScopes_FundingReadLimited() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.FUNDING_READ_LIMITED);
		assertCheckScopesFailForOtherScopes(ScopePathType.READ_PUBLIC, ScopePathType.FUNDING_READ_LIMITED);
	}

	@Test
	public void testCheckScopes_FundingCreate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.FUNDING_CREATE);
		assertCheckScopesFailForOtherScopes(ScopePathType.READ_PUBLIC, ScopePathType.FUNDING_CREATE);
	}

	@Test
	public void testCheckScopes_FundingUpdate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.FUNDING_UPDATE);
		assertCheckScopesFailForOtherScopes(ScopePathType.READ_PUBLIC, ScopePathType.FUNDING_UPDATE);
	}

	@Test
	public void testCheckScopes_PatentsReadLimited() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_PATENTS_READ_LIMITED);
		assertCheckScopesFailForOtherScopes(ScopePathType.READ_PUBLIC, ScopePathType.ORCID_PATENTS_READ_LIMITED);
	}

	@Test
	public void testCheckScopes_PatentsCreate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_PATENTS_CREATE);
		assertCheckScopesFailForOtherScopes(ScopePathType.READ_PUBLIC, ScopePathType.ORCID_PATENTS_CREATE);
	}

	@Test
	public void testCheckScopes_PatentsUpdate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_PATENTS_UPDATE);
		assertCheckScopesFailForOtherScopes(ScopePathType.READ_PUBLIC, ScopePathType.ORCID_PATENTS_UPDATE);
	}

	@Test
	public void testCheckScopes_PeerReviewReadLimited() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.PEER_REVIEW_READ_LIMITED);
		assertCheckScopesFailForOtherScopes(ScopePathType.READ_PUBLIC, ScopePathType.PEER_REVIEW_READ_LIMITED);
	}

	@Test
	public void testCheckScopes_PeerReviewCreate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.PEER_REVIEW_CREATE);
		assertCheckScopesFailForOtherScopes(ScopePathType.READ_PUBLIC, ScopePathType.PEER_REVIEW_CREATE);
	}

	@Test
	public void testCheckScopes_PeerReviewUpdate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.PEER_REVIEW_UPDATE);
		assertCheckScopesFailForOtherScopes(ScopePathType.READ_PUBLIC, ScopePathType.PEER_REVIEW_UPDATE);
	}

	@Test
	public void testCheckScopes_OrcidWorksReadLimited() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_WORKS_READ_LIMITED);
		assertCheckScopesFailForOtherScopes(ScopePathType.READ_PUBLIC, ScopePathType.ORCID_WORKS_READ_LIMITED);
	}

	@Test
	public void testCheckScopes_OrcidWorksCreate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_WORKS_CREATE);
		assertCheckScopesFailForOtherScopes(ScopePathType.READ_PUBLIC, ScopePathType.ORCID_WORKS_CREATE);
	}

	@Test
	public void testCheckScopes_OrcidWorksUpdate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_WORKS_UPDATE);
		assertCheckScopesFailForOtherScopes(ScopePathType.READ_PUBLIC, ScopePathType.ORCID_WORKS_UPDATE);
	}

	@Test
	public void testCheckScopes_ActivitiesReadLimited() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ACTIVITIES_READ_LIMITED);
		assertCheckScopesFailForOtherScopes(ScopePathType.READ_PUBLIC, ScopePathType.ACTIVITIES_READ_LIMITED,
				ScopePathType.AFFILIATIONS_READ_LIMITED, ScopePathType.FUNDING_READ_LIMITED,
				ScopePathType.ORCID_PATENTS_READ_LIMITED, ScopePathType.PEER_REVIEW_READ_LIMITED,
				ScopePathType.ORCID_WORKS_READ_LIMITED);
	}

	@Test
	public void testCheckScopes_ActivitiesUpdate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ACTIVITIES_UPDATE);
		assertCheckScopesFailForOtherScopes(ScopePathType.READ_PUBLIC, ScopePathType.ACTIVITIES_UPDATE,
				ScopePathType.ORCID_WORKS_UPDATE, ScopePathType.ORCID_WORKS_CREATE, ScopePathType.FUNDING_UPDATE,
				ScopePathType.FUNDING_CREATE, ScopePathType.AFFILIATIONS_UPDATE, ScopePathType.AFFILIATIONS_CREATE,
				ScopePathType.ORCID_PATENTS_CREATE, ScopePathType.ORCID_PATENTS_UPDATE,
				ScopePathType.PEER_REVIEW_UPDATE, ScopePathType.PEER_REVIEW_CREATE, ScopePathType.ACTIVITIES_UPDATE);
	}

	@Test
	public void testCheckScopes_OrcidProfileReadLimited() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_PROFILE_READ_LIMITED);
		assertCheckScopesFailForOtherScopes(ScopePathType.READ_PUBLIC, ScopePathType.ACTIVITIES_READ_LIMITED,
				ScopePathType.AFFILIATIONS_READ_LIMITED, ScopePathType.FUNDING_READ_LIMITED,
				ScopePathType.ORCID_PATENTS_READ_LIMITED, ScopePathType.PEER_REVIEW_READ_LIMITED,
				ScopePathType.ORCID_WORKS_READ_LIMITED, ScopePathType.ORCID_BIO_READ_LIMITED,
				ScopePathType.PERSON_READ_LIMITED, ScopePathType.READ_LIMITED,
				ScopePathType.ORCID_PROFILE_READ_LIMITED);
	}

	@Test
	public void testCheckScopes_ReadLimited() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.READ_LIMITED);
		assertCheckScopesFailForOtherScopes(ScopePathType.READ_PUBLIC, ScopePathType.ACTIVITIES_READ_LIMITED,
				ScopePathType.AFFILIATIONS_READ_LIMITED, ScopePathType.FUNDING_READ_LIMITED,
				ScopePathType.ORCID_PATENTS_READ_LIMITED, ScopePathType.PEER_REVIEW_READ_LIMITED,
				ScopePathType.ORCID_WORKS_READ_LIMITED, ScopePathType.ORCID_BIO_READ_LIMITED,
				ScopePathType.PERSON_READ_LIMITED, ScopePathType.READ_LIMITED);
	}

	@Test
	public void testCheckScopes_OrcidBioUpdate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_BIO_UPDATE);
		assertCheckScopesFailForOtherScopes(ScopePathType.READ_PUBLIC, ScopePathType.ORCID_BIO_UPDATE,
				ScopePathType.ORCID_BIO_EXTERNAL_IDENTIFIERS_CREATE);
	}

	@Test
	public void testCheckScopes_OrcidBioReadLimited() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_BIO_READ_LIMITED);
		assertCheckScopesFailForOtherScopes(ScopePathType.READ_PUBLIC, ScopePathType.ORCID_BIO_READ_LIMITED);
	}

	@Test
	public void testCheckScopes_OrcidBioExternalIdentifiersCreate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1,
				ScopePathType.ORCID_BIO_EXTERNAL_IDENTIFIERS_CREATE);
		assertCheckScopesFailForOtherScopes(ScopePathType.READ_PUBLIC,
				ScopePathType.ORCID_BIO_EXTERNAL_IDENTIFIERS_CREATE);
	}

	private void assertCheckScopesFailForOtherScopes(ScopePathType... goodOnes) {
		List<ScopePathType> list = Arrays.asList(goodOnes);
		for (ScopePathType s : ScopePathType.values()) {
			if (!list.contains(s)) {
				assertItThrowAccessControlException(s);
			} else {
				orcidSecurityManager.checkScopes(s);
			}
		}
	}

	/**
	 * =================== checkClientAccessAndScopes test's ===================
	 */
	@Test(expected = OrcidUnauthorizedException.class)
	public void testCheckClientAccessAndScopes_When_TokenIsForOtherUser() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_BIO_UPDATE);
		orcidSecurityManager.checkClientAccessAndScopes(ORCID_2, ScopePathType.ORCID_BIO_UPDATE);
		fail();
	}

	@Test
	public void testCheckClientAccessAndScopes_ReadPublic() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.READ_PUBLIC);
		assertCheckClientAccessAndScopesFailForOtherScopes(ORCID_1, ScopePathType.READ_PUBLIC);
	}

	@Test
	public void testCheckClientAccessAndScopes_Authenticate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.AUTHENTICATE);
		assertCheckClientAccessAndScopesFailForOtherScopes(ORCID_1, ScopePathType.AUTHENTICATE,
				ScopePathType.READ_PUBLIC);
	}

	@Test
	public void testCheckClientAccessAndScopes_AffiliationsReadLimited() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.AFFILIATIONS_READ_LIMITED);
		assertCheckClientAccessAndScopesFailForOtherScopes(ORCID_1, ScopePathType.READ_PUBLIC,
				ScopePathType.AFFILIATIONS_READ_LIMITED);
	}

	@Test
	public void testCheckClientAccessAndScopes_AffiliationsCreate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.AFFILIATIONS_CREATE);
		assertCheckClientAccessAndScopesFailForOtherScopes(ORCID_1, ScopePathType.READ_PUBLIC,
				ScopePathType.AFFILIATIONS_CREATE);
	}

	@Test
	public void testCheckClientAccessAndScopes_AffiliationsUpdate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.AFFILIATIONS_UPDATE);
		assertCheckClientAccessAndScopesFailForOtherScopes(ORCID_1, ScopePathType.READ_PUBLIC,
				ScopePathType.AFFILIATIONS_UPDATE);
	}

	@Test
	public void testCheckClientAccessAndScopes_FundingReadLimited() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.FUNDING_READ_LIMITED);
		assertCheckClientAccessAndScopesFailForOtherScopes(ORCID_1, ScopePathType.READ_PUBLIC,
				ScopePathType.FUNDING_READ_LIMITED);
	}

	@Test
	public void testCheckClientAccessAndScopes_FundingCreate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.FUNDING_CREATE);
		assertCheckClientAccessAndScopesFailForOtherScopes(ORCID_1, ScopePathType.READ_PUBLIC,
				ScopePathType.FUNDING_CREATE);
	}

	@Test
	public void testCheckClientAccessAndScopes_FundingUpdate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.FUNDING_UPDATE);
		assertCheckClientAccessAndScopesFailForOtherScopes(ORCID_1, ScopePathType.READ_PUBLIC,
				ScopePathType.FUNDING_UPDATE);
	}

	@Test
	public void testCheckClientAccessAndScopes_PatentsReadLimited() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_PATENTS_READ_LIMITED);
		assertCheckClientAccessAndScopesFailForOtherScopes(ORCID_1, ScopePathType.READ_PUBLIC,
				ScopePathType.ORCID_PATENTS_READ_LIMITED);
	}

	@Test
	public void testCheckClientAccessAndScopes_PatentsCreate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_PATENTS_CREATE);
		assertCheckClientAccessAndScopesFailForOtherScopes(ORCID_1, ScopePathType.READ_PUBLIC,
				ScopePathType.ORCID_PATENTS_CREATE);
	}

	@Test
	public void testCheckClientAccessAndScopes_PatentsUpdate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_PATENTS_UPDATE);
		assertCheckClientAccessAndScopesFailForOtherScopes(ORCID_1, ScopePathType.READ_PUBLIC,
				ScopePathType.ORCID_PATENTS_UPDATE);
	}

	@Test
	public void testCheckClientAccessAndScopes_PeerReviewReadLimited() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.PEER_REVIEW_READ_LIMITED);
		assertCheckClientAccessAndScopesFailForOtherScopes(ORCID_1, ScopePathType.READ_PUBLIC,
				ScopePathType.PEER_REVIEW_READ_LIMITED);
	}

	@Test
	public void testCheckClientAccessAndScopes_PeerReviewCreate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.PEER_REVIEW_CREATE);
		assertCheckClientAccessAndScopesFailForOtherScopes(ORCID_1, ScopePathType.READ_PUBLIC,
				ScopePathType.PEER_REVIEW_CREATE);
	}

	@Test
	public void testCheckClientAccessAndScopes_PeerReviewUpdate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.PEER_REVIEW_UPDATE);
		assertCheckClientAccessAndScopesFailForOtherScopes(ORCID_1, ScopePathType.READ_PUBLIC,
				ScopePathType.PEER_REVIEW_UPDATE);
	}

	@Test
	public void testCheckClientAccessAndScopes_OrcidWorksReadLimited() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_WORKS_READ_LIMITED);
		assertCheckClientAccessAndScopesFailForOtherScopes(ORCID_1, ScopePathType.READ_PUBLIC,
				ScopePathType.ORCID_WORKS_READ_LIMITED);
	}

	@Test
	public void testCheckClientAccessAndScopes_OrcidWorksCreate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_WORKS_CREATE);
		assertCheckClientAccessAndScopesFailForOtherScopes(ORCID_1, ScopePathType.READ_PUBLIC,
				ScopePathType.ORCID_WORKS_CREATE);
	}

	@Test
	public void testCheckClientAccessAndScopes_OrcidWorksUpdate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_WORKS_UPDATE);
		assertCheckClientAccessAndScopesFailForOtherScopes(ORCID_1, ScopePathType.READ_PUBLIC,
				ScopePathType.ORCID_WORKS_UPDATE);
	}

	@Test
	public void testCheckClientAccessAndScopes_ActivitiesReadLimited() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ACTIVITIES_READ_LIMITED);
		assertCheckClientAccessAndScopesFailForOtherScopes(ORCID_1, ScopePathType.READ_PUBLIC,
				ScopePathType.ACTIVITIES_READ_LIMITED, ScopePathType.AFFILIATIONS_READ_LIMITED,
				ScopePathType.FUNDING_READ_LIMITED, ScopePathType.ORCID_PATENTS_READ_LIMITED,
				ScopePathType.PEER_REVIEW_READ_LIMITED, ScopePathType.ORCID_WORKS_READ_LIMITED);
	}

	@Test
	public void testCheckClientAccessAndScopes_ActivitiesUpdate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ACTIVITIES_UPDATE);
		assertCheckClientAccessAndScopesFailForOtherScopes(ORCID_1, ScopePathType.READ_PUBLIC,
				ScopePathType.ACTIVITIES_UPDATE, ScopePathType.ORCID_WORKS_UPDATE, ScopePathType.ORCID_WORKS_CREATE,
				ScopePathType.FUNDING_UPDATE, ScopePathType.FUNDING_CREATE, ScopePathType.AFFILIATIONS_UPDATE,
				ScopePathType.AFFILIATIONS_CREATE, ScopePathType.ORCID_PATENTS_CREATE,
				ScopePathType.ORCID_PATENTS_UPDATE, ScopePathType.PEER_REVIEW_UPDATE, ScopePathType.PEER_REVIEW_CREATE,
				ScopePathType.ACTIVITIES_UPDATE);
	}

	@Test
	public void testCheckClientAccessAndScopes_OrcidProfileReadLimited() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_PROFILE_READ_LIMITED);
		assertCheckClientAccessAndScopesFailForOtherScopes(ORCID_1, ScopePathType.READ_PUBLIC,
				ScopePathType.ACTIVITIES_READ_LIMITED, ScopePathType.AFFILIATIONS_READ_LIMITED,
				ScopePathType.FUNDING_READ_LIMITED, ScopePathType.ORCID_PATENTS_READ_LIMITED,
				ScopePathType.PEER_REVIEW_READ_LIMITED, ScopePathType.ORCID_WORKS_READ_LIMITED,
				ScopePathType.ORCID_BIO_READ_LIMITED, ScopePathType.PERSON_READ_LIMITED, ScopePathType.READ_LIMITED,
				ScopePathType.ORCID_PROFILE_READ_LIMITED);
	}

	@Test
	public void testCheckClientAccessAndScopes_ReadLimited() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.READ_LIMITED);
		assertCheckClientAccessAndScopesFailForOtherScopes(ORCID_1, ScopePathType.READ_PUBLIC,
				ScopePathType.ACTIVITIES_READ_LIMITED, ScopePathType.AFFILIATIONS_READ_LIMITED,
				ScopePathType.FUNDING_READ_LIMITED, ScopePathType.ORCID_PATENTS_READ_LIMITED,
				ScopePathType.PEER_REVIEW_READ_LIMITED, ScopePathType.ORCID_WORKS_READ_LIMITED,
				ScopePathType.ORCID_BIO_READ_LIMITED, ScopePathType.PERSON_READ_LIMITED, ScopePathType.READ_LIMITED);
	}

	@Test
	public void testCheckClientAccessAndScopes_OrcidBioUpdate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_BIO_UPDATE);
		assertCheckClientAccessAndScopesFailForOtherScopes(ORCID_1, ScopePathType.READ_PUBLIC,
				ScopePathType.ORCID_BIO_UPDATE, ScopePathType.ORCID_BIO_EXTERNAL_IDENTIFIERS_CREATE);
	}

	@Test
	public void testCheckClientAccessAndScopes_OrcidBioReadLimited() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_BIO_READ_LIMITED);
		assertCheckClientAccessAndScopesFailForOtherScopes(ORCID_1, ScopePathType.READ_PUBLIC,
				ScopePathType.ORCID_BIO_READ_LIMITED);
	}

	@Test
	public void testCheckClientAccessAndScopes_OrcidBioExternalIdentifiersCreate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1,
				ScopePathType.ORCID_BIO_EXTERNAL_IDENTIFIERS_CREATE);
		assertCheckClientAccessAndScopesFailForOtherScopes(ORCID_1, ScopePathType.READ_PUBLIC,
				ScopePathType.ORCID_BIO_EXTERNAL_IDENTIFIERS_CREATE);
	}

	private void assertCheckClientAccessAndScopesFailForOtherScopes(String orcid, ScopePathType... goodOnes) {
		List<ScopePathType> list = Arrays.asList(goodOnes);
		for (ScopePathType s : ScopePathType.values()) {
			if (!list.contains(s)) {
				assertItThrowAccessControlException(orcid, s);
			} else {
				orcidSecurityManager.checkClientAccessAndScopes(orcid, s);
			}
		}
	}

	/**
	 * =================== checkAndFilter test's ===================
	 */
	// ---- ELEMENTS WITHOUT SOURCE ----
	// Name element tests
	@Test
	public void testName_CanRead_When_ReadPublicToken_IsPublic() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.READ_PUBLIC);
		Name name = createName(Visibility.PUBLIC);
		orcidSecurityManager.checkAndFilter(ORCID_1, name, ScopePathType.ORCID_BIO_READ_LIMITED);
	}

	@Test(expected = AccessControlException.class)
	public void testName_CantRead_When_ReadPublicToken_IsLimited() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.READ_PUBLIC);
		Name name = createName(Visibility.LIMITED);
		orcidSecurityManager.checkAndFilter(ORCID_1, name, ScopePathType.ORCID_BIO_READ_LIMITED);
		fail();
	}

	@Test(expected = AccessControlException.class)
	public void testName_CantRead_When_ReadPublicToken_IsPrivate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.READ_PUBLIC);
		Name name = createName(Visibility.PRIVATE);
		orcidSecurityManager.checkAndFilter(ORCID_1, name, ScopePathType.ORCID_BIO_READ_LIMITED);
		fail();
	}

	@Test(expected = OrcidUnauthorizedException.class)
	public void testName_ThrowException_When_TokenIsForOtherUser() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_BIO_UPDATE);
		Name name = createName(Visibility.PUBLIC);
		orcidSecurityManager.checkAndFilter(ORCID_2, name, ScopePathType.ORCID_BIO_READ_LIMITED);
		fail();
	}

	@Test
	public void testName_CanRead_When_DontHaveReadScope_IsPublic() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_BIO_UPDATE);
		Name name = createName(Visibility.PUBLIC);
		orcidSecurityManager.checkAndFilter(ORCID_1, name, ScopePathType.ORCID_BIO_READ_LIMITED);
	}

	@Test(expected = AccessControlException.class)
	public void testName_CantRead_When_DontHaveReadScope_IsLimited() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_BIO_UPDATE);
		Name name = createName(Visibility.LIMITED);
		orcidSecurityManager.checkAndFilter(ORCID_1, name, ScopePathType.ORCID_BIO_READ_LIMITED);
		fail();
	}

	@Test(expected = AccessControlException.class)
	public void testName_CantRead_When_DontHaveReadScope_IsPrivate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_BIO_UPDATE);
		Name name = createName(Visibility.PRIVATE);
		orcidSecurityManager.checkAndFilter(ORCID_1, name, ScopePathType.ORCID_BIO_READ_LIMITED);
		fail();
	}

	@Test
	public void testName_CanRead_When_HaveReadScope_IsPublic() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_BIO_READ_LIMITED);
		Name name = createName(Visibility.PUBLIC);
		orcidSecurityManager.checkAndFilter(ORCID_1, name, ScopePathType.ORCID_BIO_READ_LIMITED);
	}

	@Test
	public void testName_CanRead_When_HaveReadScope_IsLimited() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_BIO_READ_LIMITED);
		Name name = createName(Visibility.LIMITED);
		orcidSecurityManager.checkAndFilter(ORCID_1, name, ScopePathType.ORCID_BIO_READ_LIMITED);
	}

	@Test(expected = OrcidVisibilityException.class)
	public void testName_CantRead_When_HaveReadScope_IsPrivate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_BIO_READ_LIMITED);
		Name name = createName(Visibility.PRIVATE);
		orcidSecurityManager.checkAndFilter(ORCID_1, name, ScopePathType.ORCID_BIO_READ_LIMITED);
		fail();
	}

	// Biography element tests
	@Test
	public void testBio_CanRead_When_ReadPublicToken_IsPublic() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.READ_PUBLIC);
		Biography bio = createBiography(Visibility.PUBLIC);
		orcidSecurityManager.checkAndFilter(ORCID_1, bio, ScopePathType.ORCID_BIO_READ_LIMITED);
	}

	@Test(expected = AccessControlException.class)
	public void testBio_CantRead_When_ReadPublicToken_IsLimited() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.READ_PUBLIC);
		Biography bio = createBiography(Visibility.LIMITED);
		orcidSecurityManager.checkAndFilter(ORCID_1, bio, ScopePathType.ORCID_BIO_READ_LIMITED);
		fail();
	}

	@Test(expected = AccessControlException.class)
	public void testBio_CantRead_When_ReadPublicToken_IsPrivate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.READ_PUBLIC);
		Biography bio = createBiography(Visibility.PRIVATE);
		orcidSecurityManager.checkAndFilter(ORCID_1, bio, ScopePathType.ORCID_BIO_READ_LIMITED);
		fail();
	}

	@Test(expected = OrcidUnauthorizedException.class)
	public void testBio_ThrowException_When_TokenIsForOtherUser() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_BIO_UPDATE);
		Biography bio = createBiography(Visibility.PUBLIC);
		orcidSecurityManager.checkAndFilter(ORCID_2, bio, ScopePathType.ORCID_BIO_READ_LIMITED);
		fail();
	}

	@Test
	public void testBio_CanRead_When_DontHaveReadScope_IsPublic() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_BIO_UPDATE);
		Biography bio = createBiography(Visibility.PUBLIC);
		orcidSecurityManager.checkAndFilter(ORCID_1, bio, ScopePathType.ORCID_BIO_READ_LIMITED);
	}

	@Test(expected = AccessControlException.class)
	public void testBio_CantRead_When_DontHaveReadScope_IsLimited() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_BIO_UPDATE);
		Biography bio = createBiography(Visibility.LIMITED);
		orcidSecurityManager.checkAndFilter(ORCID_1, bio, ScopePathType.ORCID_BIO_READ_LIMITED);
		fail();
	}

	@Test(expected = AccessControlException.class)
	public void testBio_CantRead_When_DontHaveReadScope_IsPrivate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_BIO_UPDATE);
		Biography bio = createBiography(Visibility.PRIVATE);
		orcidSecurityManager.checkAndFilter(ORCID_1, bio, ScopePathType.ORCID_BIO_READ_LIMITED);
		fail();
	}

	@Test
	public void testBio_CanRead_When_HaveReadScope_IsPublic() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_BIO_READ_LIMITED);
		Biography bio = createBiography(Visibility.PUBLIC);
		orcidSecurityManager.checkAndFilter(ORCID_1, bio, ScopePathType.ORCID_BIO_READ_LIMITED);
	}

	@Test
	public void testBio_CanRead_When_HaveReadScope_IsLimited() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_BIO_READ_LIMITED);
		Biography bio = createBiography(Visibility.LIMITED);
		orcidSecurityManager.checkAndFilter(ORCID_1, bio, ScopePathType.ORCID_BIO_READ_LIMITED);
	}

	@Test(expected = OrcidVisibilityException.class)
	public void testBio_CantRead_When_HaveReadScope_IsPrivate() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_BIO_READ_LIMITED);
		Biography bio = createBiography(Visibility.PRIVATE);
		orcidSecurityManager.checkAndFilter(ORCID_1, bio, ScopePathType.ORCID_BIO_READ_LIMITED);
		fail();
	}

	// ---- ELEMENTS WITH SOURCE ----
	// Work element tests
	@Test
	public void testWork_CanRead_When_IsSource_And_ReadLimitedToken() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_WORKS_READ_LIMITED);

		Work work = createWork(Visibility.PUBLIC, CLIENT_1);
		orcidSecurityManager.checkAndFilter(ORCID_1, work, ScopePathType.ORCID_WORKS_READ_LIMITED);

		work = createWork(Visibility.LIMITED, CLIENT_1);
		orcidSecurityManager.checkAndFilter(ORCID_1, work, ScopePathType.ORCID_WORKS_READ_LIMITED);

		work = createWork(Visibility.PRIVATE, CLIENT_1);
		orcidSecurityManager.checkAndFilter(ORCID_1, work, ScopePathType.ORCID_WORKS_READ_LIMITED);
	}

	@Test
	public void testWork_CanRead_When_ReadPublicToken_IsPublic_NotSource() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.READ_PUBLIC);
		Work work = createWork(Visibility.PUBLIC, CLIENT_2);
		orcidSecurityManager.checkAndFilter(ORCID_1, work, ScopePathType.ORCID_WORKS_READ_LIMITED);
	}

	@Test(expected = AccessControlException.class)
	public void testWork_CantRead_When_ReadPublicToken_IsLimited_NotSource() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.READ_PUBLIC);
		Work work = createWork(Visibility.LIMITED, CLIENT_2);
		orcidSecurityManager.checkAndFilter(ORCID_1, work, ScopePathType.ORCID_WORKS_READ_LIMITED);
		fail();
	}

	@Test(expected = AccessControlException.class)
	public void testWork_CantRead_When_ReadPublicToken_IsPrivate_NotSource() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.READ_PUBLIC);
		Work work = createWork(Visibility.PRIVATE, CLIENT_2);
		orcidSecurityManager.checkAndFilter(ORCID_1, work, ScopePathType.ORCID_WORKS_READ_LIMITED);
		fail();
	}

	@Test(expected = OrcidUnauthorizedException.class)
	public void testWork_ThrowException_When_TokenIsForOtherUser_IsSource() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.READ_PUBLIC);
		Work work = createWork(Visibility.PUBLIC, CLIENT_1);
		orcidSecurityManager.checkAndFilter(ORCID_2, work, ScopePathType.ORCID_WORKS_READ_LIMITED);
		fail();
	}

	@Test(expected = OrcidUnauthorizedException.class)
	public void testWork_ThrowException_When_TokenIsForOtherUser_NotSource() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.READ_PUBLIC);
		Work work = createWork(Visibility.PUBLIC, CLIENT_2);
		orcidSecurityManager.checkAndFilter(ORCID_2, work, ScopePathType.ORCID_WORKS_READ_LIMITED);
		fail();
	}

	@Test
	public void testWork_CanRead_When_IsSource() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.READ_PUBLIC);

		Work work = createWork(Visibility.PUBLIC, CLIENT_1);
		orcidSecurityManager.checkAndFilter(ORCID_1, work, ScopePathType.ORCID_WORKS_READ_LIMITED);

		work = createWork(Visibility.LIMITED, CLIENT_1);
		orcidSecurityManager.checkAndFilter(ORCID_1, work, ScopePathType.ORCID_WORKS_READ_LIMITED);

		work = createWork(Visibility.PRIVATE, CLIENT_1);
		orcidSecurityManager.checkAndFilter(ORCID_1, work, ScopePathType.ORCID_WORKS_READ_LIMITED);
	}

	@Test
	public void testWork_CanRead_When_DontHaveReadScope_IsPublic_NotSource() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_WORKS_CREATE);
		Work work = createWork(Visibility.PUBLIC, CLIENT_2);
		orcidSecurityManager.checkAndFilter(ORCID_1, work, ScopePathType.ORCID_WORKS_READ_LIMITED);
	}

	@Test(expected = AccessControlException.class)
	public void testWork_CantRead_When_DontHaveReadScope_IsLimited_NotSource() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_WORKS_CREATE);
		Work work = createWork(Visibility.LIMITED, CLIENT_2);
		orcidSecurityManager.checkAndFilter(ORCID_1, work, ScopePathType.ORCID_WORKS_READ_LIMITED);
		fail();
	}

	@Test(expected = AccessControlException.class)
	public void testWork_CantRead_When_DontHaveReadScope_IsPrivate_NotSource() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_WORKS_CREATE);
		Work work = createWork(Visibility.PRIVATE, CLIENT_2);
		orcidSecurityManager.checkAndFilter(ORCID_1, work, ScopePathType.ORCID_WORKS_READ_LIMITED);
		fail();
	}

	@Test
	public void testWork_CanRead_When_HaveReadScope_IsPublic_NotSource() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_WORKS_READ_LIMITED);
		Work work = createWork(Visibility.PUBLIC, CLIENT_2);
		orcidSecurityManager.checkAndFilter(ORCID_1, work, ScopePathType.ORCID_WORKS_READ_LIMITED);
	}

	@Test
	public void testWork_CanRead_When_HaveReadScope_IsLimited_NotSource() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_WORKS_READ_LIMITED);
		Work work = createWork(Visibility.LIMITED, CLIENT_2);
		orcidSecurityManager.checkAndFilter(ORCID_1, work, ScopePathType.ORCID_WORKS_READ_LIMITED);
	}

	@Test(expected = OrcidVisibilityException.class)
	public void testWork_CantRead_When_HaveReadScope_IsPrivate_NotSource() {
		SecurityContextTestUtils.setUpSecurityContext(ORCID_1, CLIENT_1, ScopePathType.ORCID_WORKS_READ_LIMITED);
		Work work = createWork(Visibility.PRIVATE, CLIENT_2);
		orcidSecurityManager.checkAndFilter(ORCID_1, work, ScopePathType.ORCID_WORKS_READ_LIMITED);
		fail();
	}

	/**
	 * Utilities
	 */
	private void assertItThrowAccessControlException(String orcid, ScopePathType s) {
		try {
			orcidSecurityManager.checkClientAccessAndScopes(orcid, s);
			fail();
		} catch (AccessControlException e) {
			return;
		} catch (Exception e) {
			fail();
		}
		fail();
	}

	private void assertItThrowAccessControlException(ScopePathType s) {
		try {
			orcidSecurityManager.checkScopes(s);
			fail();
		} catch (AccessControlException e) {
			return;
		} catch (Exception e) {
			fail();
		}
		fail();
	}

	private Biography createBiography(Visibility v) {
		return new Biography("Biography", v);
	}

	private OtherName createOtherName(Visibility v, String sourceId) {
		OtherName otherName = new OtherName();
		otherName.setContent("other-name");
		otherName.setVisibility(v);
		Source source = new Source();
		source.setSourceClientId(new SourceClientId(sourceId));
		otherName.setSource(source);
		return otherName;
	}

	private Work createWork(Visibility v, String sourceId) {
		Work work = new Work();
		work.setVisibility(v);
		Source source = new Source();
		source.setSourceClientId(new SourceClientId(sourceId));
		work.setSource(source);
		return work;
	}

	private Name createName(Visibility v) {
		Name name = new Name();
		name.setVisibility(v);
		name.setCreditName(new CreditName("Credit Name"));
		return name;
	}

	private ProfileEntity createProfileEntity() {
		ProfileEntity entity = new ProfileEntity();
		entity.setClaimed(true);
		entity.setDeactivationDate(null);
		entity.setDeprecatedDate(null);
		entity.setId("0000-0000-0000-0000");
		entity.setRecordLocked(false);
		entity.setSource(new SourceEntity(new ClientDetailsEntity("APP-0000000000000000")));
		entity.setSubmissionDate(null);
		return entity;
	}
}
