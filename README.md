<!--
 ___ _            _ _    _ _    __
/ __(_)_ __  _ __| (_)__(_) |_ /_/
\__ \ | '  \| '_ \ | / _| |  _/ -_)
|___/_|_|_|_| .__/_|_\__|_|\__\___|
            |_| 
-->
![](https://docs.simplicite.io//logos/logo250.png)
* * *

`ProjectManagement` module definition
=====================================

Project management modules with tickets and tasks management.

`PmGroup` business object definition
------------------------------------

Groups of rights of the module.

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|

`PmResponsability` business object definition
---------------------------------------------

Users’ responsibilities on groups in the module.

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `rsp_login_id` link to **`PmUser`**                          | id                                       | yes*     | yes       |          | -                                                                                |
| `rsp_group_id` link to **`PmGroup`**                         | id                                       | yes*     | yes       |          | -                                                                                |
| `row_module_id` link to **`Module`**                         | id                                       | yes      | yes       |          | Module                                                                           |
| _Ref. `row_module_id.mdl_name`_                              | _regexp(100)_                            |          |           |          | _Module name_                                                                    |

`PmTaskHistoric` business object definition
-------------------------------------------

Historic of tasks

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `row_ref_id` link to **`PmTask`**                            | id                                       | yes*     |           |          | Record row ID                                                                    |
| `row_idx`                                                    | int(11)                                  | yes*     | yes       |          | History record index                                                             |
| `created_by_hist`                                            | char(100)                                | yes*     |           |          | Created by                                                                       |
| `created_dt_hist`                                            | datetime                                 | yes*     |           |          | Created date                                                                     |
| `pmTskNumber`                                                | char(100)                                | yes*     |           |          | -                                                                                |
| `pmTskTitle`                                                 | char(100)                                | yes      | yes       |          | -                                                                                |
| `pmTskDescription`                                           | text(4000)                               |          | yes       |          | -                                                                                |
| `pmTskStatus`                                                | enum(100) using `PM_TSK_STATUT` list     | yes      | yes       |          | -                                                                                |
| `pmTskPriority`                                              | enum(100) using `PM_TSK_PRIORITE` list   | yes      | yes       |          | -                                                                                |
| `pmTskClose`                                                 | date                                     | yes      | yes       |          | -                                                                                |
| `pmTskVrsId` link to **`PmVersion`**                         | id                                       | yes*     | yes       |          | -                                                                                |
| _Ref. `pmTskVrsId.pmVrsVersion`_                             | _char(100)_                              |          |           |          | -                                                                                |
| _Ref. `pmTskVrsId.pmVrsPrjId`_                               | _id_                                     |          |           |          | -                                                                                |
| _Ref. `pmVrsPrjId.pmPrjName`_                                | _char(100)_                              |          |           |          | -                                                                                |
| _Ref. `pmTskVrsId.pmVrsName`_                                | _char(200)_                              |          |           |          | _Concatenation of project name and version number_                               |
| `pmTskType`                                                  | enum(100) using `PM_TSK_TYPE` list       | yes      | yes       |          | -                                                                                |

### Lists

* `PM_TSK_STATUT`
    - `DRAFT` Draft
    - `TODO` To do
    - `DOING` Doing
    - `DONE` Done
    - `CLOSED` Closed
    - `CANCEL` Cancel
    - `REJECTED` Rejected
* `PM_TSK_PRIORITE`
    - `LOW` Faible
    - `NORMAL` Normal
    - `HIGH` Haute
    - `URGENT` Urgent
    - `IMMEDIATE` Immédiat
* `PM_TSK_TYPE`
    - `BUG` Defect
    - `MEE` Meeting
    - `TSK` TASK

`PmUser` business object definition
-----------------------------------

Users of the module.

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `pmUsrNbTask`                                                | int(100)                                 |          |           |          | -                                                                                |
| `usr_lang`                                                   | enum(3) using `LANG` list                | yes      | yes       | yes      | Language                                                                         |
| `usr_active`                                                 | enum(1) using `USER_STATUS` list         |          | yes       |          | -                                                                                |
| `usr_home_id` link to **`View`**                             | id                                       |          | yes       |          | -                                                                                |
| _Ref. `usr_home_id.viw_name`_                                | _char(100)_                              |          |           |          | -                                                                                |
| `row_module_id` link to **`Module`**                         | id                                       | yes      | yes       |          | Module                                                                           |
| _Ref. `row_module_id.mdl_name`_                              | _regexp(100)_                            |          |           |          | _Module name_                                                                    |

### Lists

* `LANG`
    - `ENU` English language
    - `FRA` French language
* `USER_STATUS`
    - `0` Disabled
    - `1` Enabled
    - `2` Pending
    - `3` Web services only

`PmProject` business object definition
--------------------------------------



### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `pmPrjName`                                                  | char(100)                                | yes*     | yes       |          | -                                                                                |
| `pmPrjService`                                               | enum(100) using `PM_PRJ_SERVICE` list    | yes      | yes       |          | -                                                                                |
| `pmPrjDescription`                                           | text(1000)                               |          | yes       |          | -                                                                                |
| `pmPrjBudget`                                                | float(100, 2)                            |          | yes       |          | -                                                                                |

### Lists

* `PM_PRJ_SERVICE`
    - `SERVICEA` Service A
    - `SERVICEB` Service B
    - `SERVICEC` Service C

`PmVersion` business object definition
--------------------------------------

Project version

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `pmVrsVersion`                                               | char(100)                                | yes*     | yes       |          | -                                                                                |
| `pmVrsPublicationDate`                                       | date                                     | yes      | yes       |          | -                                                                                |
| `pmVrsStatus`                                                | enum(100) using `PM_VRS_STATUS` list     | yes      | yes       |          | -                                                                                |
| `pmVrsName`                                                  | char(200)                                |          |           |          | Concatenation of project name and version number                                 |
| `pmVrsPrjId` link to **`PmProject`**                         | id                                       | yes*     | yes       |          | -                                                                                |
| _Ref. `pmVrsPrjId.pmPrjName`_                                | _char(100)_                              |          |           |          | -                                                                                |
| `pmVrsCompletion`                                            | int(100)                                 |          |           |          | -                                                                                |

### Lists

* `PM_VRS_STATUS`
    - `ALPHA` Alpha
    - `BETA` Beta
    - `PUBLISHED` Published version

### Custom actions

* `PM_DEFER_TASK`: 
* `PM_UPDATE_GANTT`: 

`PmTask` business object definition
-----------------------------------

Tasks of project

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `pmTskNumber`                                                | char(100)                                | yes*     |           |          | -                                                                                |
| `pmTskTitle`                                                 | char(100)                                | yes      | yes       |          | -                                                                                |
| `pmTskDescription`                                           | text(4000)                               |          | yes       |          | -                                                                                |
| `pmTskStatus`                                                | enum(100) using `PM_TSK_STATUT` list     | yes      | yes       |          | -                                                                                |
| `pmTskPriority`                                              | enum(100) using `PM_TSK_PRIORITE` list   | yes      | yes       |          | -                                                                                |
| `pmTskCreation`                                              | date                                     | yes      | yes       |          | -                                                                                |
| `pmTskClose`                                                 | date                                     | yes      | yes       |          | -                                                                                |
| `pmTskEffectiveClosingDate`                                  | date                                     |          |           |          | -                                                                                |
| `pmTskExpectedDuration`                                      | int(100)                                 | yes      |           |          | -                                                                                |
| `pmTskActualDuration`                                        | int(100)                                 |          |           |          | -                                                                                |
| `pmTskTimeLeft`                                              | int(100)                                 |          |           |          | -                                                                                |
| `pmTskUsrId` link to **`PmUser`**                            | id                                       |          |           |          | -                                                                                |
| _Ref. `pmTskUsrId.usr_login`_                                | _regexp(100)_                            |          |           | yes      | _Login_                                                                          |
| _Ref. `pmTskUsrId.usr_first_name`_                           | _char(50)_                               |          |           | yes      | _First name_                                                                     |
| _Ref. `pmTskUsrId.usr_last_name`_                            | _char(50)_                               |          |           | yes      | _Last name_                                                                      |
| `pmTskPrjVirtualId` link to **`PmProject`**                  | id                                       |          | yes       |          | -                                                                                |
| `pmTskVrsId` link to **`PmVersion`**                         | id                                       | yes*     | yes       |          | -                                                                                |
| _Ref. `pmTskVrsId.pmVrsVersion`_                             | _char(100)_                              |          |           |          | -                                                                                |
| _Ref. `pmTskVrsId.pmVrsPrjId`_                               | _id_                                     |          |           |          | -                                                                                |
| _Ref. `pmVrsPrjId.pmPrjName`_                                | _char(100)_                              |          |           |          | -                                                                                |
| _Ref. `pmTskVrsId.pmVrsStatus`_                              | _enum(100) using `PM_VRS_STATUS` list_   |          |           |          | -                                                                                |
| _Ref. `pmTskVrsId.pmVrsName`_                                | _char(200)_                              |          |           |          | _Concatenation of project name and version number_                               |
| _Ref. `pmTskVrsId.pmVrsPublicationDate`_                     | _date_                                   |          |           |          | -                                                                                |
| `pmTskCompletion`                                            | int(100)                                 | yes      | yes       |          | -                                                                                |
| `pmTskType`                                                  | enum(100) using `PM_TSK_TYPE` list       | yes      | yes       |          | -                                                                                |
| `pmTskLastCron`                                              | datetime                                 |          |           |          | -                                                                                |

### Lists

* `PM_TSK_STATUT`
    - `DRAFT` Draft
    - `TODO` To do
    - `DOING` Doing
    - `DONE` Done
    - `CLOSED` Closed
    - `CANCEL` Cancel
    - `REJECTED` Rejected
* `PM_TSK_PRIORITE`
    - `LOW` Faible
    - `NORMAL` Normal
    - `HIGH` Haute
    - `URGENT` Urgent
    - `IMMEDIATE` Immédiat
* `PM_VRS_STATUS`
    - `ALPHA` Alpha
    - `BETA` Beta
    - `PUBLISHED` Published version
* `PM_TSK_TYPE`
    - `BUG` Defect
    - `MEE` Meeting
    - `TSK` TASK

### Custom actions

* `PM_ASSIGN`: 
* `PM_TASK_MSG_DELETION`: 
* `PM_TASK_NOTIF`: 
* `PM_UPDATE_GANTT`: 

`PmArrayOfTask` business object definition
------------------------------------------

dependency between tasks

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `pmAotPrvTskId` link to **`PmTask`**                         | id                                       | yes*     | yes       |          | -                                                                                |
| _Ref. `pmAotPrvTskId.pmTskNumber`_                           | _char(100)_                              |          |           |          | -                                                                                |
| _Ref. `pmAotPrvTskId.pmTskVrsId`_                            | _id_                                     |          |           |          | -                                                                                |
| _Ref. `pmTskVrsId.pmVrsVersion`_                             | _char(100)_                              |          |           |          | -                                                                                |
| _Ref. `pmAotPrvTskId.pmTskStatus`_                           | _enum(100) using `PM_TSK_STATUT` list_   |          |           |          | -                                                                                |
| _Ref. `pmTskVrsId.pmVrsPrjId`_                               | _id_                                     |          |           |          | -                                                                                |
| _Ref. `pmVrsPrjId.pmPrjName`_                                | _char(100)_                              |          |           |          | -                                                                                |
| `pmAotNextTskId` link to **`PmTask`**                        | id                                       | yes*     | yes       |          | -                                                                                |
| _Ref. `pmAotNextTskId.pmTskNumber`_                          | _char(100)_                              |          |           |          | -                                                                                |
| _Ref. `pmAotNextTskId.pmTskVrsId`_                           | _id_                                     |          |           |          | -                                                                                |
| _Ref. `pmTskVrsId.pmVrsVersion`_                             | _char(100)_                              |          |           |          | -                                                                                |
| _Ref. `pmAotNextTskId.pmTskStatus`_                          | _enum(100) using `PM_TSK_STATUT` list_   |          |           |          | -                                                                                |
| _Ref. `pmTskVrsId.pmVrsPrjId`_                               | _id_                                     |          |           |          | -                                                                                |
| _Ref. `pmVrsPrjId.pmPrjName`_                                | _char(100)_                              |          |           |          | -                                                                                |

### Lists

* `PM_TSK_STATUT`
    - `DRAFT` Draft
    - `TODO` To do
    - `DOING` Doing
    - `DONE` Done
    - `CLOSED` Closed
    - `CANCEL` Cancel
    - `REJECTED` Rejected

`PmDocument` business object definition
---------------------------------------

Useful documents for a task or a project

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `pmDocAttachment`                                            | document                                 |          | yes       |          | -                                                                                |
| `pmDocTitle`                                                 | char(100)                                | yes*     | yes       |          | -                                                                                |
| `pmDocTskId` link to **`PmTask`**                            | id                                       |          | yes       |          | -                                                                                |
| _Ref. `pmDocTskId.pmTskNumber`_                              | _char(100)_                              |          |           |          | -                                                                                |
| _Ref. `pmDocTskId.pmTskVrsId`_                               | _id_                                     |          |           |          | -                                                                                |
| _Ref. `pmTskVrsId.pmVrsVersion`_                             | _char(100)_                              |          |           |          | -                                                                                |
| _Ref. `pmTskVrsId.pmVrsPrjId`_                               | _id_                                     |          |           |          | -                                                                                |
| _Ref. `pmDocTskId.pmTskTitle`_                               | _char(100)_                              |          |           |          | -                                                                                |
| _Ref. `pmVrsPrjId.pmPrjName`_                                | _char(100)_                              |          |           |          | -                                                                                |
| `pmDocPrjId` link to **`PmProject`**                         | id                                       |          | yes       |          | -                                                                                |
| _Ref. `pmDocPrjId.pmPrjName`_                                | _char(100)_                              |          |           |          | -                                                                                |
| `pmDocStatus`                                                | enum(100) using `PM_DOC_STATUS` list     | yes      | yes       |          | -                                                                                |
| `pmDocType`                                                  | enum(100) using `PM_DOC_TYPE` list       | yes      | yes       |          | -                                                                                |

### Lists

* `PM_DOC_STATUS`
    - `WFV` Waiting for validation
    - `V` Valid
* `PM_DOC_TYPE`
    - `REQ` Required
    - `DOC` Document
    - `SPEC` Specification

`PmRole` business object definition
-----------------------------------



### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `pmRolPrjId` link to **`PmProject`**                         | id                                       | yes*     | yes       |          | -                                                                                |
| _Ref. `pmRolPrjId.pmPrjName`_                                | _char(100)_                              |          |           |          | -                                                                                |
| `pmRolUsrId` link to **`PmUser`**                            | id                                       | yes*     | yes       |          | -                                                                                |
| _Ref. `pmRolUsrId.usr_login`_                                | _regexp(100)_                            |          |           | yes      | _Login_                                                                          |
| `pmRolRole`                                                  | enum(100) using `PM_ROL_ROLE` list       | yes      | yes       |          | -                                                                                |

### Lists

* `PM_ROL_ROLE`
    - `ADMIN` Admin
    - `USER` User
    - `MANAGER` Manager

`PmMessage` business object definition
--------------------------------------

Messaging on a task

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `pmMsgTitle`                                                 | char(100)                                | yes*     | yes       |          | -                                                                                |
| `pmMsgMessage`                                               | text(100)                                |          | yes       |          | -                                                                                |
| `pmMsgPublicationDate`                                       | date                                     | yes      |           |          | -                                                                                |
| `pmMsgUsrId` link to **`PmUser`**                            | id                                       | yes*     |           |          | -                                                                                |
| _Ref. `pmMsgUsrId.usr_login`_                                | _regexp(100)_                            |          |           | yes      | _Login_                                                                          |
| `pmMsgTskId` link to **`PmTask`**                            | id                                       | *        | yes       |          | -                                                                                |
| _Ref. `pmMsgTskId.pmTskNumber`_                              | _char(100)_                              |          |           |          | -                                                                                |
| _Ref. `pmMsgTskId.pmTskVrsId`_                               | _id_                                     |          |           |          | -                                                                                |
| _Ref. `pmTskVrsId.pmVrsVersion`_                             | _char(100)_                              |          |           |          | -                                                                                |
| _Ref. `pmMsgTskId.pmTskTitle`_                               | _char(100)_                              |          |           |          | -                                                                                |
| _Ref. `pmTskVrsId.pmVrsPrjId`_                               | _id_                                     |          |           |          | -                                                                                |
| _Ref. `pmVrsPrjId.pmPrjName`_                                | _char(100)_                              |          |           |          | -                                                                                |

`PmAssignment` business object definition
-----------------------------------------

Assignment of a user to a task, allows the establishment of a timesheet

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `pmAssRole`                                                  | enum(100) using `PM_ASS_ROLE` list       | yes      | yes       |          | -                                                                                |
| `pmAssPmUserid` link to **`PmUser`**                         | id                                       | yes*     | yes       |          | -                                                                                |
| _Ref. `pmAssPmUserid.usr_login`_                             | _regexp(100)_                            |          |           | yes      | _Login_                                                                          |
| _Ref. `pmAssPmUserid.pmUsrNbTask`_                           | _int(100)_                               |          |           |          | -                                                                                |
| `pmAssPmTaskid` link to **`PmTask`**                         | id                                       | yes*     | yes       |          | -                                                                                |
| _Ref. `pmAssPmTaskid.pmTskNumber`_                           | _char(100)_                              |          |           |          | -                                                                                |
| _Ref. `pmAssPmTaskid.pmTskTitle`_                            | _char(100)_                              |          |           |          | -                                                                                |
| _Ref. `pmAssPmTaskid.pmTskVrsId`_                            | _id_                                     |          |           |          | -                                                                                |
| _Ref. `pmTskVrsId.pmVrsName`_                                | _char(200)_                              |          |           |          | _Concatenation of project name and version number_                               |
| _Ref. `pmTskVrsId.pmVrsPrjId`_                               | _id_                                     |          |           |          | -                                                                                |
| _Ref. `pmAssPmTaskid.pmTskStatus`_                           | _enum(100) using `PM_TSK_STATUT` list_   |          |           |          | -                                                                                |
| _Ref. `pmTskVrsId.pmVrsVersion`_                             | _char(100)_                              |          |           |          | -                                                                                |
| _Ref. `pmVrsPrjId.pmPrjName`_                                | _char(100)_                              |          |           |          | -                                                                                |
| _Ref. `pmAssPmTaskid.pmTskPriority`_                         | _enum(100) using `PM_TSK_PRIORITE` list_ |          |           |          | -                                                                                |
| _Ref. `pmAssPmTaskid.pmTskCreation`_                         | _date_                                   |          |           |          | -                                                                                |
| _Ref. `pmAssPmTaskid.pmTskClose`_                            | _date_                                   |          |           |          | -                                                                                |
| `pmAssQuantity`                                              | float(6, 2)                              |          | yes       |          | -                                                                                |
| `pmAssConsumed`                                              | float(100, 2)                            | yes      |           |          | -                                                                                |

### Lists

* `PM_ASS_ROLE`
    - `DEVELOPER` Developpeur
    - `MANAGER` Manager
* `PM_TSK_STATUT`
    - `DRAFT` Draft
    - `TODO` To do
    - `DOING` Doing
    - `DONE` Done
    - `CLOSED` Closed
    - `CANCEL` Cancel
    - `REJECTED` Rejected
* `PM_TSK_PRIORITE`
    - `LOW` Faible
    - `NORMAL` Normal
    - `HIGH` Haute
    - `URGENT` Urgent
    - `IMMEDIATE` Immédiat

`PmAssignmentPmTimeSheetAssign` business object definition
----------------------------------------------------------

Timesheet of PmAssignmentPmTimeSheetAssign

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `tsh_parent_id` link to **`PmAssignment`**                   | id                                       | yes*     | yes       |          | Assign reference                                                                 |
| `tsh_year`                                                   | char(4)                                  | yes*     | yes       |          | Year                                                                             |
| `tsh_month`                                                  | char(2)                                  | yes*     | yes       |          | Month                                                                            |
| `tsh_input1`                                                 | char(255)                                |          | yes       |          | -                                                                                |
| `tsh_total1`                                                 | float(11, 2)                             |          | yes       |          | -                                                                                |
| _Ref. `tsh_parent_id.pmAssPmUserid`_                         | _id_                                     |          |           |          | -                                                                                |
| _Ref. `pmAssPmUserid.usr_login`_                             | _regexp(100)_                            |          |           | yes      | _Login_                                                                          |
| _Ref. `tsh_parent_id.pmAssPmTaskid`_                         | _id_                                     |          |           |          | -                                                                                |
| _Ref. `pmAssPmTaskid.pmTskNumber`_                           | _char(100)_                              |          |           |          | -                                                                                |
| _Ref. `pmAssPmTaskid.pmTskVrsId`_                            | _id_                                     |          |           |          | -                                                                                |
| _Ref. `pmTskVrsId.pmVrsPrjId`_                               | _id_                                     |          |           |          | -                                                                                |
| _Ref. `pmVrsPrjId.pmPrjName`_                                | _char(100)_                              |          |           |          | -                                                                                |
| _Ref. `pmTskVrsId.pmVrsVersion`_                             | _char(100)_                              |          |           |          | -                                                                                |

`PmLabel` business object definition
------------------------------------

task label, useful for task categorization

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `pmLblName`                                                  | char(100)                                | yes*     | yes       |          | -                                                                                |
| `pmLblIcone`                                                 | image                                    |          | yes       |          | -                                                                                |

`PmTskLbl` business object definition
-------------------------------------

Labeling of tasks

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `pmTsklblTskId` link to **`PmTask`**                         | id                                       | yes*     | yes       |          | -                                                                                |
| _Ref. `pmTsklblTskId.pmTskNumber`_                           | _char(100)_                              |          |           |          | -                                                                                |
| _Ref. `pmTsklblTskId.pmTskVrsId`_                            | _id_                                     |          |           |          | -                                                                                |
| _Ref. `pmTskVrsId.pmVrsVersion`_                             | _char(100)_                              |          |           |          | -                                                                                |
| _Ref. `pmTsklblTskId.pmTskTitle`_                            | _char(100)_                              |          |           |          | -                                                                                |
| _Ref. `pmTskVrsId.pmVrsPrjId`_                               | _id_                                     |          |           |          | -                                                                                |
| _Ref. `pmVrsPrjId.pmPrjName`_                                | _char(100)_                              |          |           |          | -                                                                                |
| `pmTsklblLblId` link to **`PmLabel`**                        | id                                       | yes*     | yes       |          | -                                                                                |
| _Ref. `pmTsklblLblId.pmLblName`_                             | _char(100)_                              |          |           |          | -                                                                                |
| _Ref. `pmTskVrsId.pmVrsVersion`_                             | _char(100)_                              |          |           |          | -                                                                                |

`PmTaskCreate` business process definition
------------------------------------------



### Activities

* `Begin`: Begin activity
* `ProjectSelect`: Select the project to which the task belongs
* `VersionSelect`: Select the version on which to add the task
* `TaskCreation`: Creation of the task
* `PreviousTask`: Selection of the task on which the task depends
* `NextTask`: Selection of the task which will depend on the task
* `Labelling`: 
* `Assignment`: Reflective creation of all assignment
* `End`: End activity

`PmProjectCalendar` external object definition
----------------------------------------------

External object for display calendar of versions and tasks on project form.


`PmProjectGantt` external object definition
-------------------------------------------

External object for display gantt of versions and tasks on project form.


`PmProjectUserTask` external object definition
----------------------------------------------

External object for display user task on specific project in project form.


`PmTaskWAssingExt` external object definition
---------------------------------------------

External object for display unassigned  task in menu


`PmTimesheetExt` external object definition
-------------------------------------------

External object for display user imputation in menu.


`PmTimesheetRecapExt` external object definition
------------------------------------------------

External object for display details on user imputation view.


