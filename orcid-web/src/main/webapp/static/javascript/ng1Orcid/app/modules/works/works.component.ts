declare var ActSortState: any;
declare var GroupedActivities: any;
declare var om: any;
declare var openImportWizardUrl: any;

import { NgForOf, NgIf } 
    from '@angular/common'; 

import { AfterViewInit, Component, OnDestroy, OnInit } 
    from '@angular/core';

import { Observable, Subject, Subscription } 
    from 'rxjs';
import { takeUntil } 
    from 'rxjs/operators';

import { CommonService } 
    from '../../shared/common.service.ts';

import { WorksService } 
    from '../../shared/works.service.ts';

import { EmailService } 
    from '../../shared/email.service.ts';

import { ModalService } 
    from '../../shared/modal.service.ts';

import { WorkspaceService } 
    from '../../shared/workspace.service.ts'; 

@Component({
    selector: 'works-ng2',
    template:  scriptTmpl("works-ng2-template")
})
export class WorksComponent implements AfterViewInit, OnDestroy, OnInit {
    private ngUnsubscribe: Subject<void> = new Subject<void>();
    private subscription: Subscription;

    addingWork: boolean;
    bibtexExportError: boolean;
    bibtexLoading: boolean;
    bibtexParsingError: boolean;
    bibtextWork: boolean;
    bibtextWorkIndex: any;
    bulkChecked: any;
    bulkDeleteCount: number;
    bulkDeleteSubmit: boolean;
    bulkDisplayToggle: false;
    bulkEditMap: any;
    bulkEditShow: boolean;
    combineWork: any;
    delCountVerify: number;
    displayURLPopOver: any;
    editSources: any;
    editWork: any;
    emails: any;
    emailSrvc: any;
    formData: any;
    geoArea: any;
    loadingScripts: any;
    moreInfo: any;
    moreInfoOpen: boolean;
    noLinkFlag: boolean;
    scriptsLoaded: boolean;
    selectedGeoArea: any;
    selectedWorkType: any;
    showBibtex: any;
    showBibtexExport: boolean;
    showBibtexImportWizard: boolean;
    showElement: any;
    sortState: any;
    textFiles: any;
    wizardDescExpanded: any;
    workImportWizard: boolean;
    workImportWizardsOriginal: any;
    workType: any;
    worksFromBibtex: any;

    constructor( 
        private commonSrvc: CommonService,
        private emailService: EmailService,
        private modalService: ModalService,
        private workspaceSrvc: WorkspaceService,
        private worksService: WorksService
    ) {
        //console.log('works component init');

        this.addingWork = false;
        this.bibtexExportError = false;
        this.bibtexLoading = false;
        this.bibtexParsingError = false;
        this.bibtextWork = false;
        this.bibtextWorkIndex = null;
        this.bulkChecked = false;
        this.bulkDeleteCount = 0;
        this.bulkDeleteSubmit = false;
        this.bulkDisplayToggle = false;
        this.bulkEditMap = {};
        this.bulkEditShow = false;
        this.combineWork = null;
        this.delCountVerify = 0;
        this.displayURLPopOver = {};
        this.editSources = {};
        this.editWork = null;
        this.emails = {};
        this.formData = {
            works: null
        };
        this.geoArea = ['All'];
        this.loadingScripts = false;
        this.moreInfo = {};
        this.moreInfoOpen = false;
        this.noLinkFlag = true;
        this.scriptsLoaded = false;
        this.selectedGeoArea = null;
        this.selectedWorkType = null;
        this.showBibtex = {};
        this.showBibtexExport = false;
        this.showBibtexImportWizard = false;
        this.showElement = {};
        this.sortState = new ActSortState(GroupedActivities.ABBR_WORK);
        this.textFiles = [];
        this.wizardDescExpanded = {};
        this.workImportWizard = false;
        this.workImportWizardsOriginal = null;
        this.workType = ['All'];
        this.worksFromBibtex = null;
    }

    addExternalIdentifier(): void {
        this.editWork.workExternalIdentifiers.push(
            {
                relationship: {
                    value: "self"
                }, 
                url: {
                    value: ""
                },
                workExternalIdentifierId: {
                    value: ""
                }, 
                workExternalIdentifierType: {
                    value: ""
                } 
            }
        );
    };

    addWorkFromBibtex(work): void {
        this.bibtextWork = true;              
        this.bibtextWorkIndex = this.worksFromBibtex.indexOf(work);     
        this.editWork = this.worksFromBibtex[this.bibtextWorkIndex];
        
        this.putWork();        
    };

    addWorkModal(work): void {
        this.emailService.getEmails()
        .pipe(    
            takeUntil(this.ngUnsubscribe)
        )
        .subscribe(
            data => {
                this.emails = data;
                console.log('open add aff modal');
                if( this.emailService.getEmailPrimary().verified ){
                    this.worksService.notifyOther({ work:work });
                    if(work == undefined) {
                        this.modalService.notifyOther({action:'open', moduleId: 'modalWorksForm', edit: false});
                    } else {
                        this.modalService.notifyOther({action:'open', moduleId: 'modalWorksForm', edit: true});
                    }                    
                }else{
                    this.modalService.notifyOther({action:'open', moduleId: 'modalemailunverified'});
                }
            },
            error => {
                //console.log('getEmails', error);
            } 
        );
        /*
        if(emailVerified === true 
            || configuration.showModalManualEditVerificationEnabled == false
        ){
            if (data == undefined) {
                worksService.getBlankWork(function(data) {
                    $scope.editWork = data;
                    $timeout(function(){
                        $scope.loadWorkTypes();
                        $scope.showAddWorkModal();
                    });
                });
            } else {
                $scope.editWork = data;
                if( $scope.editWork.workExternalIdentifiers.length == 0 ){
                    $scope.addExternalIdentifier();
                }        
                $scope.loadWorkTypes();
                $scope.showAddWorkModal();
            }
        } else {
            showEmailVerificationModal();
        }
        */
    };

    bibtexShowToggle(putCode): void {
        this.showBibtex[putCode] = !(this.showBibtex[putCode]);
    };

    bulkChangeAll(bool): void {
        this.bulkChecked = bool;
        this.bulkDisplayToggle = false;
        for (var idx in this.worksService.groups){
            this.bulkEditMap[this.worksService.groups[idx].activePutCode] = bool;
        }
    };

    canBeCombined(work): any {
        if (this.userIsSource(work)){
            return true;
        }
        return this.hasCombineableEIs(work);
    };

    closePopover(event): void {
        this.moreInfoOpen = false;
        $('.work-more-info-container').css('display', 'none');
    };

    deleteBulkConfirm(idx): void {
        var idx: any;
        this.bulkDeleteCount = 0;
        this.bulkDeleteSubmit = false;        
        this.delCountVerify = 0;
        for (idx in this.worksService.groups){
            if (this.bulkEditMap[this.worksService.groups[idx].activePutCode]){
                this.bulkDeleteCount++;
            }
        }

        /*
        this.bulkDeleteFunction = this.deleteBulk;

        $.colorbox({
            html: $compile($('#bulk-delete-modal').html())($scope)
        });
        $.colorbox.resize();
        */
    };

    deleteWorkConfirm(putCode, deleteGroup): void {
        let maxSize = 100;
        let work = this.worksService.getWork(putCode);
        /*
        this.deletePutCode = putCode;
        this.deleteGroup = deleteGroup;
        if (work.title){
            this.fixedTitle = work.title.value;
        }
        else {  
            this.fixedTitle = '';
        } 
        if(this.fixedTitle.length > maxSize){
            this.fixedTitle = this.fixedTitle.substring(0, maxSize) + '...';
        }

        $.colorbox({
            html : $compile($('#delete-work-modal').html())($scope),
            onComplete: function() {$.colorbox.resize();}
        });
        */
    };

    editWorkFromBibtex(work): void {
        this.bibtextWork = true;
        this.bibtextWorkIndex = this.worksFromBibtex.indexOf(work);
        
        this.addWorkModal(this.worksFromBibtex[this.bibtextWorkIndex]);        
    };

    fetchBibtexExport(){
        this.bibtexLoading = true;
        this.bibtexExportError = false; 
        
        /*
        $.ajax({
            url: getBaseUri() + '/' + 'works/works.bib',
            type: 'GET',
            success: function(data) {
                this.bibtexLoading = false;
                if(window.navigator.msSaveOrOpenBlob) {
                    var fileData = [data];
                    blobObject = new Blob(fileData, {type: 'text/plain'});
                    window.navigator.msSaveOrOpenBlob(blobObject, "works.bib");                              
                } else {
                    var anchor = angular.element('<a/>');
                    anchor.css({display: 'none'});
                    angular.element(document.body).append(anchor);
                    anchor.attr({
                        href: 'data:text/x-bibtex;charset=utf-8,' + encodeURIComponent(data),
                        target: '_self',
                        download: 'works.bib'
                    })[0].click();
                    anchor.remove();
                }
            }
        }).fail(function() {
            this.bibtexExportError = true;
            //console.log("bibtex export error");
        });  
        */      
    };

    hasCombineableEIs(work): boolean {
        if (work.workExternalIdentifiers != null){
            for (var idx in work.workExternalIdentifiers){
                if (work.workExternalIdentifiers[idx].workExternalIdentifierType.value != 'issn'){
                    return true;
                }
            }
        }
        return false;
    };

    hideSources(group): void {
        this.editSources[group.groupId] = false;
    };

    hideTooltip(key): void {        
        this.showElement[key] = false;
    };

    hideURLPopOver(id): void {       
        this.displayURLPopOver[id] = false;
    };

    loadDetails(putCode, event): void {
        //Close any open popover
        this.closePopover(event);
        this.moreInfoOpen = true;
        //Display the popover
        $(event.target).next().css('display','inline');
        this.worksService.getGroupDetails(
            putCode, 
            this.worksService.constants.access_type.USER
        );
    };

    loadMore(): void {
        this.worksService.addAbbrWorksToScope(this.worksService.constants.access_type.USER, this.sortState.predicateKey, 
            !this.sortState.reverseKey[this.sortState.predicateKey]
        )
        .takeUntil(this.ngUnsubscribe)
        .subscribe(
            data => {
                this.formData = data;

                console.log('this.getForm works', this.formData);

                //let itemVisibility = null;
                //let len = null;

                this.formData = data;
                this.worksService.handleWorkGroupData( this.formData );
                //this.newElementDefaultVisibility = this.formData.visibility.visibility;
                this.worksService.loading = false;
            },
            error => {
                this.worksService.loading = false;
                //console.log('getWorksFormError', error);
            } 
        );
    };

    loadWorkImportWizardList(): void {
        this.worksService.loadWorkImportWizardList()
        .pipe(    
            takeUntil(this.ngUnsubscribe)
        )
        .subscribe(
            data => {
                //console.log('this.getForm works loadWorkImportWizardList', data);

                if(data == null || data.length == 0) {
                    this.noLinkFlag = false;
                }
                this.selectedWorkType = om.get('workspace.works.import_wizzard.all');
                this.selectedGeoArea = om.get('workspace.works.import_wizzard.all');
                this.workImportWizardsOriginal = data;
                this.bulkEditShow = false;
                this.showBibtexImportWizard = false;
                for(var idx in data) {                            
                    for(var i in data[idx].actTypes) {
                        if(!this.commonSrvc.contains(this.workType, data[idx].actTypes[i])) {
                            this.workType.push(data[idx].actTypes[i]);
                        }                                
                    }
                    for(var j in data[idx].geoAreas) {
                        if(!this.commonSrvc.contains(this.geoArea, data[idx].geoAreas[j])) {
                            this.geoArea.push(data[idx].geoAreas[j]);
                        }                                
                    }                            
                }
            },
            error => {
                //console.log('WorkImportWizardError', error);
            } 
        );
    };

    openBibTextWizard(): void {
        this.emailService.getEmails()
        .pipe(    
            takeUntil(this.ngUnsubscribe)
        )
        .subscribe(
            data => {
                this.emails = data;
                if( this.emailService.getEmailPrimary().verified ){
                    this.bibtexParsingError = false;
                    this.bulkEditShow = false;
                    this.showBibtexExport = false;
                    this.showBibtexImportWizard = !(this.showBibtexImportWizard);
                    this.workImportWizard = false;
                    this.worksFromBibtex = null;
                }else{
                    this.modalService.notifyOther({action:'open', moduleId: 'modalemailunverified'});
                }
            },
            error => {
                //console.log('getEmails', error);
            } 
        );
    };

    openFileDialog(): void{
        this.textFiles = [];
        this.bibtexParsingError = false;
        /*
        $timeout(
            function() { //To avoid '$apply already in progress' error
                angular.element('#inputBibtex').trigger('click');
            }, 
            0
        );
        */
    };

    openImportWizardUrl(url): void {
        openImportWizardUrl(url);
    };

    openImportWizardUrlFilter(url, client): void {
        url = url + '?client_id=' + client.id + '&response_type=code&scope=' + client.scopes + '&redirect_uri=' + client.redirectUri;
        openImportWizardUrl(url);
    };

    putWork(): any{
        this.emailService.getEmails()
        .pipe(    
            takeUntil(this.ngUnsubscribe)
        )
        .subscribe(
            data => {
                /*
                this.emails = data;
                if( this.emailService.getEmailPrimary().verified ){
                    if ( this.addingWork ) {
                        return; // don't process if adding work
                    }
                    this.addingWork = true;
                    this.editWork.errors.length = 0;
                    this.worksService.putWork(
                        this.editWork,
                        function(data){
                            if (data.errors.length == 0) {
                                if (this.bibtextWork == false){
                                    $.colorbox.close();
                                    $scope.addingWork = false;
                                } else {
                                    $timeout(function(){
                                        $scope.worksFromBibtex.splice($scope.bibtextWorkIndex, 1);
                                        $scope.bibtextWork = false;
                                        $scope.addingWork = false;
                                    });
                                    $.colorbox.close();
                                    $scope.worksService.addAbbrWorksToScope(worksService.constants.access_type.USER, $scope.sortState.predicateKey, !$scope.sortState.reverseKey[$scope.sortState.predicateKey]);
                                }
                            } else {
                                $timeout(function(){
                                    $scope.editWork = data;                    
                                    commonSrvc.copyErrorsLeft($scope.editWork, data);
                                    $scope.addingWork = false;
                                });
                                // make sure colorbox is shown if there are errors
                                if (!($("#colorbox").css("display")=="block")) {
                                    $scope.addWorkModal(data);
                                }
                            }
                        },
                        function() {
                            // something bad is happening!
                            $scope.addingWork = false;
                        }
                    );
                }else{
                    this.modalService.notifyOther({action:'open', moduleId: 'modalemailunverified'});
                }
                */
            },
            error => {
                //console.log('getEmails', error);
            } 
        );
    };

    rmWorkFromBibtex(work): void {
        let index = this.worksFromBibtex.indexOf(work);
        
        this.worksFromBibtex.splice(index, 1);
    };

    saveAllFromBibtex(): any{
        var worksToSave = null;
        var numToSave = null;
        /*
        if( savingBibtex == false ){
            savingBibtex = true;

            worksToSave =  new Array();
            angular.forEach($scope.worksFromBibtex, function( work, key ) {
                if (work.errors.length == 0){
                    worksToSave.push(work);
                } 
            });
            
            numToSave = worksToSave.length;
            angular.forEach( worksToSave, function( work, key ) {
                worksService.putWork(work,function(data) {
                    $timeout(function(){
                        var index = $scope.worksFromBibtex.indexOf(work);
                        $scope.worksFromBibtex.splice(index, 1);
                    });
                    numToSave--;
                    if (numToSave == 0){
                        $scope.closeAllMoreInfo();
                        $scope.worksService.refreshWorkGroups($scope.sortState.predicateKey, !$scope.sortState.reverseKey[$scope.sortState.predicateKey]);
                        savingBibtex = false;
                    }
                });
            });

        }
        */
    };

    setGroupPrivacy(putCode, priv): void {
        var group = this.worksService.getGroup(putCode);
        console.log(group);
        var putCodes = new Array();
        for (var idx in group.works) {
            putCodes.push(group.works[idx].putCode.value);
            group.works[idx].visibility.visibility = priv;
        }
        this.worksService.updateVisibility(putCodes, priv)
        .takeUntil(this.ngUnsubscribe)
        .subscribe(
            data => {

                if (putCodes.length > 0) {
                    this.worksService.updateVisibility(putCodes, priv);
                }

                group.activeVisibility = priv;
            },
            error => {
                console.log('Error updating group visibility', error);
            } 
        );
    }

    setPrivacy(putCodes, priv): void {
        //this.worksService.updateVisibility([putCode], priv); 
        this.worksService.updateVisibility(putCodes, priv)
        .takeUntil(this.ngUnsubscribe)
        .subscribe(
            data => {
                if (putCodes.length > 0) {
                    this.worksService.updateVisibility(putCodes, priv);
                }
            },
            error => {
                console.log('Error updating work visibility', error);
            } 
        ); 
    }

    showCombineMatches( work1 ): void {
        this.combineWork = work1;
        /*
        $.colorbox({
            scrolling: true,
            html: $compile($('#combine-work-template').html())($scope),
            onLoad: function() {$('#cboxClose').remove();},
            // start the colorbox off with the correct width
            width: this.commonSrvc.formColorBoxResize(),
            onComplete: function() {$.colorbox.resize();},
            onClosed: function() {
                $scope.closeAllMoreInfo();
                $scope.worksService.refreshWorkGroups($scope.sortState.predicateKey, !$scope.sortState.reverseKey[$scope.sortState.predicateKey]);
            }
        });
        */
    };

    showDetailsMouseClick = function(group, $event) {
        //console.log('showDetailsMouseClick envent', $event);
        $event.stopPropagation();
        this.moreInfo[group.groupId] = !this.moreInfo[group.groupId];
        for (var idx in group.works){
            this.loadDetails(group.works[idx].putCode.value, $event);
        }
    };

    showMozillaBadges(putCode): any{
        /*
        $scope.$watch(
            function () { 
                return document.getElementsByClassName('badge-container-' + putCode).length; 
            },
            function (newValue, oldValue) {
                  if (newValue !== oldValue) {
                      if ($scope.badgesRequested[putCode] == null){
                        var dois = worksService.getUniqueDois(putCode);
                        var c = document.getElementsByClassName('badge-container-' + putCode);
                        for (var i = 0; i <= dois.length - 1; i++){
                            var code = 'var conf={"article-doi": "' + dois[i].trim() + '", "container-class": "badge-container-' + putCode + '"};showBadges(conf);';
                            var s = document.createElement('script');
                            s.type = 'text/javascript';
                            try {
                                s.appendChild(document.createTextNode(code));
                                c[0].appendChild(s);
                            } catch (e) {
                                s.text = code;
                                c[0].appendChild(s);
                            }
                        }
                        $scope.badgesRequested[putCode] = true;
                    }
                }
            }
        );
        */
    };

    showSources(group): void {
        this.editSources[group.groupId] = true;
    };

    showTooltip(key): void{        
        this.showElement[key] = true;     
    };

    showURLPopOver(id): void{       
        this.displayURLPopOver[id] = true;
    };

    showWorkImportWizard(): void {
        if(!this.workImportWizard) {
            this.loadWorkImportWizardList();
        }
        this.workImportWizard = !this.workImportWizard;
    }; 

    sort = function(key) {
        this.sortState.sortBy(key);
        this.worksService.resetWorkGroups();
        this.worksService.addAbbrWorksToScope( 
            this.sortState.predicateKey, 
            !this.sortState.reverseKey[key]
        );
       
    };

    toggleBibtexExport(): void{
        this.bibtexExportError = false;
        this.bibtexLoading = false;
        this.bibtexParsingError = false;
        this.bulkEditShow = false;        
        this.loadingScripts = false;
        this.scriptsLoaded = false;
        this.showBibtexExport  = !this.showBibtexExport;
        this.showBibtexImportWizard = false;
        this.workImportWizard = false;
    };

    toggleBulkEdit(): void {
        this.emailService.getEmails()
        .pipe(    
            takeUntil(this.ngUnsubscribe)
        )
        .subscribe(
            data => {
                this.emails = data;
                if( this.emailService.getEmailPrimary().verified ){
                    this.worksService.loadAllWorkGroups(
                    this.sortState.predicateKey, 
                    !this.sortState.reverseKey[this.sortState.predicateKey], 
                    function() {
                        if (!this.bulkEditShow) {
                            
                            this.bulkChecked = false;
                            for (var idx in this.worksService.groups){
                                this.bulkEditMap[this.worksService.groups[idx].activePutCode] = false;
                            }
                        };
                        this.bulkEditShow = !this.bulkEditShow;
                        this.showBibtexImportWizard = false;
                        this.workImportWizard = false;
                        this.showBibtexExport = false;
                    }
                );
                }else{
                    this.modalService.notifyOther({action:'open', moduleId: 'modalemailunverified'});
                }
            },
            error => {
                //console.log('getEmails', error);
            } 
        );
    };

    toggleWizardDesc(id): void {
        this.wizardDescExpanded[id] = !this.wizardDescExpanded[id];
    };

    userIsSource(work): boolean {
        if (work.source == orcidVar.orcidId){
            return true;
        }
        return false;
    };

    //Default init functions provided by Angular Core
    ngAfterViewInit() {
        //Fire functions AFTER the view inited. Useful when DOM is required or access children directives
        this.subscription = this.worksService.notifyObservable$.subscribe(
            (res) => {
                this.loadMore();
            }
        );
    };

    ngOnDestroy() {
        this.ngUnsubscribe.next();
        this.ngUnsubscribe.complete();
    };

    ngOnInit() {
        this.loadMore();
        this.loadWorkImportWizardList();
    };
}