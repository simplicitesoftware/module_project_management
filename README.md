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

Module de gestion de projet et de gestion de tâche ou de tickets.

`PmArrayOfTask` business object definition
------------------------------------------

This table represents the link between two tasks. A task can have a previous task and a next task.

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `pmAotTskId` link to **`PmTask`**                            | id                                       | yes*     | yes       |          | -                                                                                |
| _Ref. `pmAotTskId.pmTskNumber`_                              | _int(100)_                               |          |           |          | -                                                                                |
| `pmAotTskId` link to **`PmTask`**                            | id                                       | yes*     | yes       |          | -                                                                                |
| _Ref. `pmAotTskId.pmTskNumber`_                              | _int(100)_                               |          |           |          | -                                                                                |

`PmAssignment` business object definition
-----------------------------------------



### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `pmAssRole`                                                  | text(100)                                | yes      | yes       |          | -                                                                                |
| `pmAssPmUserid` link to **`PmUser`**                         | id                                       |          | yes       |          | -                                                                                |
| _Ref. `pmAssPmUserid.usr_login`_                             | _regexp(100)_                            |          |           | yes      | _Login_                                                                          |
| `pmAssPmTaskid` link to **`PmTask`**                         | id                                       |          | yes       |          | -                                                                                |
| _Ref. `pmAssPmTaskid.pmTskNumber`_                           | _int(100)_                               |          |           |          | -                                                                                |
| `pmAssId`                                                    | int(100)                                 | yes*     | yes       |          | -                                                                                |
| _Ref. `pmAssPmTaskid.pmTskTitle`_                            | _char(100)_                              |          |           |          | -                                                                                |

`PmDocument` business object definition
---------------------------------------

Useful documents for a task or a project

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `pmDocAttachment`                                            | document                                 |          | yes       |          | -                                                                                |
| `pmDocTitle`                                                 | char(100)                                | yes*     | yes       |          | -                                                                                |
| `pmDocTskId` link to **`PmTask`**                            | id                                       |          | yes       |          | -                                                                                |
| _Ref. `pmDocTskId.pmTskNumber`_                              | _int(100)_                               |          |           |          | -                                                                                |
| `pmDocPrjId` link to **`PmProject`**                         | id                                       |          | yes       |          | -                                                                                |
| _Ref. `pmDocPrjId.pmPrjName`_                                | _char(100)_                              |          |           |          | -                                                                                |

`PmLabel` business object definition
------------------------------------



### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `pmLblName`                                                  | char(100)                                | yes*     | yes       |          | -                                                                                |
| `pmLblIcone`                                                 | image                                    |          | yes       |          | -                                                                                |

`PmMessage` business object definition
--------------------------------------

Messaging

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `pmMsgTitle`                                                 | char(100)                                | yes*     | yes       |          | -                                                                                |
| _Ref. `pmTskMsgId.pmTskNumber`_                              | _int(100)_                               |          |           |          | -                                                                                |
| `pmMsgMessage`                                               | text(100)                                |          | yes       |          | -                                                                                |
| `pmMsgVisibility`                                            | enum(100) using `PM_MSG_VISIBILITY` list |          | yes       |          | -                                                                                |
| `pmMsgPublicationDate`                                       | date                                     | yes      | yes       |          | -                                                                                |
| `pmTskMsgId` link to **`PmTask`**                            | id                                       |          | yes       |          | -                                                                                |
| `pmMsgUsrId` link to **`PmUser`**                            | id                                       |          | yes       |          | -                                                                                |
| _Ref. `pmMsgUsrId.usr_login`_                                | _regexp(100)_                            |          |           | yes      | _Login_                                                                          |

### Lists

* `PM_MSG_VISIBILITY`
    - `ADMIN` Admin
    - `ALL` All

`PmProject` business object definition
--------------------------------------



### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `pmPrjName`                                                  | char(100)                                | yes*     | yes       |          | -                                                                                |
| `pmPrjService`                                               | enum(100) using `PM_PRJ_SERVICE` list    | yes      | yes       |          | -                                                                                |
| `pmPrjDescription`                                           | text(100)                                |          | yes       |          | -                                                                                |
| `pmPrjBudget`                                                | float(100, 2)                            |          | yes       |          | -                                                                                |

### Lists

* `PM_PRJ_SERVICE`
    - `SERVICEA` Service A
    - `SERVICEB` Service B
    - `SERVICEC` Service C

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

`PmTask` business object definition
-----------------------------------

Task of project

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `pmTskNumber`                                                | int(100)                                 | yes*     | yes       |          | -                                                                                |
| `pmTskTitle`                                                 | char(100)                                |          | yes       |          | -                                                                                |
| `pmTskDescription`                                           | text(100)                                |          | yes       |          | -                                                                                |
| `pmTskStatus`                                                | enum(100) using `PM_TSK_STATUT` list     | yes      | yes       |          | -                                                                                |
| `pmTskPriority`                                              | enum(100) using `PM_TSK_PRIORITE` list   | yes      | yes       |          | -                                                                                |
| `pmTskCreation`                                              | date                                     | yes      | yes       |          | -                                                                                |
| `pmTskClose`                                                 | date                                     |          | yes       |          | -                                                                                |
| `pmTskExpectedDuration`                                      | int(100)                                 | yes      | yes       |          | -                                                                                |
| `pmTskActualDuration`                                        | int(100)                                 |          | yes       |          | -                                                                                |
| `pmTskVrsId` link to **`PmVersion`**                         | id                                       |          | yes       |          | -                                                                                |
| _Ref. `pmTskVrsId.pmVrsVersion`_                             | _float(100, 2)_                          |          |           |          | -                                                                                |
| _Ref. `pmTskVrsId.pmVrsPrjId`_                               | _id_                                     |          |           |          | -                                                                                |
| `pmTskUsrId` link to **`PmUser`**                            | id                                       |          | yes       |          | -                                                                                |
| _Ref. `pmTskUsrId.usr_login`_                                | _regexp(100)_                            |          |           | yes      | _Login_                                                                          |

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
    - `LOW` Low
    - `NORMAL` Normal
    - `HIGH` Hight
    - `Urgent` Urgent
    - `IMMEDIATE` Immediate

`PmTskLbl` business object definition
-------------------------------------



### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `pmTsklblTskId` link to **`PmTask`**                         | id                                       | yes*     | yes       |          | -                                                                                |
| _Ref. `pmTsklblTskId.pmTskNumber`_                           | _int(100)_                               |          |           |          | -                                                                                |
| `pmTsklblLblId` link to **`PmLabel`**                        | id                                       | yes*     | yes       |          | -                                                                                |
| _Ref. `pmTsklblLblId.pmLblName`_                             | _char(100)_                              |          |           |          | -                                                                                |

`PmVersion` business object definition
--------------------------------------

Project version

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `pmVrsVersion`                                               | float(100, 2)                            | yes*     | yes       |          | -                                                                                |
| `pmVrsPublicationDate`                                       | date                                     |          | yes       |          | -                                                                                |
| `pmVrsStatus`                                                | enum(100) using `PM_VRS_STATUS` list     | yes      | yes       |          | -                                                                                |
| `pmVrsName`                                                  | text(120)                                |          | yes       |          | Concatenation of project name and version number                                 |
| `pmVrsPrjId` link to **`PmProject`**                         | id                                       | yes      | yes       |          | -                                                                                |
| _Ref. `pmVrsPrjId.pmPrjName`_                                | _char(100)_                              |          |           |          | -                                                                                |

### Lists

* `PM_VRS_STATUS`
    - `ALPHA` Alpha
    - `BETA` Beta
    - `PUBLISHED` Published version

