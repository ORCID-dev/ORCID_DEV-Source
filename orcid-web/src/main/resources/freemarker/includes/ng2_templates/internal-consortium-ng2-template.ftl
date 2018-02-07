<#--

    =============================================================================

    ORCID (R) Open Source
    http://orcid.org

    Copyright (c) 2012-2014 ORCID, Inc.
    Licensed under an MIT-Style License (MIT)
    http://orcid.org/open-source-license

    This copyright and license information (including a link to the full license)
    shall be included in its entirety in all copies or substantial portion of
    the software.

    =============================================================================

-->

<script type="text/ng-template" id="internal-consortium-ng2">
    <div class="workspace-accordion-item" >           
        <p>
            <a *ngIf="showFindModal" (click)="toggleFindConsortiumModal()"><span class="glyphicon glyphicon-chevron-down blue"></span><@orcid.msg 'manage_members.manage_consortia'/></a>
            <a *ngIf="!showFindModal" (click)="toggleFindConsortiumModal()"><span class="glyphicon glyphicon-chevron-right blue"></span><@orcid.msg 'manage_members.manage_consortia'/></a>             
        </p>
        <div class="collapsible bottom-margin-small admin-modal" id="find_consortium_modal" *ngIf="showFindModal">    
            <!-- Find -->
            <div class="form-group">
                <div>
                    <label for="salesForceId"><@orcid.msg 'manage_consortium.salesforce_id' /></label>
                    <input type="text" id="salesForceId" (keyup.enter)="findConsortium()" [(ngModel)]="salesForceId" placeholder="<@orcid.msg 'manage_consortium.salesforce_id' />" class="input-xlarge" />                   
                </div>  
                <span *ngIf="findConsortiumError"  class="orcid-error">
                    <@spring.message "manage_consortium.salesforce_id_not_found"/>
                </span>
                <div class="controls save-btns pull-left">
                    <span id="bottom-search" (click)="findConsortium()" class="btn btn-primary"><@orcid.msg 'admin.edit_client.find'/></span>
                </div>  
            </div>
            <!-- Edit consortium -->
            <div *ngIf="consortium != null" >
                <div class="admin-edit-consortium">
                    <div class="row">
                        <h3><@orcid.msg 'manage_consortium.consortium_lead'/></h3>
                    </div>                      
                    <div class="row">
                        <!-- Name -->
                        <p>{{consortium.name.value}}</p>
                    </div>
                    <div class="row">
                    <!-- Contacts -->
                        <h4><@orcid.msg 'manage_consortium.contacts_heading'/></h4>
                        <table>
                            <tr>
                                <th>Name</th><th>Email</th><th>Role</th><th>ORCID iD</th><th>Self-service enabled</th>
                            </tr>
                            <tr *ngFor="let contact of consortium.contactsList">
                                <td>{{contact.name}}</td><td>{{contact.email}}</td><td>{{consortium.roleMap[contact.role.roleType]}}</td><td>{{contact.orcid}}</td><td>{{contact.selfServiceEnabled}}</td>
                            </tr>
                        </table>
                    </div>
                    <!-- Buttons -->
                    <div class="row">
                        <div class="controls save-btns col-md-12 col-sm-12 col-xs-12">
                            <span id="bottom-confirm-update-consortium" (click)="confirmUpdateConsortium()" class="btn btn-primary"><@orcid.msg 'manage_member.edit_member.btn.update'/></span>
                        </div>
                    </div>
                    <div class="form-group" *ngIf="success_edit_member_message != null">
                        <div [innerHTML]="success_edit_member_message" class="alert alert-success"></div>
                    </div>                      
                </div>
            </div>
        </div>
    </div>
</script>