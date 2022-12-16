# API Reference 🔌

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### checkPermissions()

```typescript
checkPermissions() => Promise<PermissionStatus>
```

**Returns:** <code>Promise&lt;<a href="#permissionstatus">PermissionStatus</a>&gt;</code>

--------------------


### requestPermissions()

```typescript
requestPermissions() => Promise<PermissionStatus>
```

**Returns:** <code>Promise&lt;<a href="#permissionstatus">PermissionStatus</a>&gt;</code>

--------------------


### getContact(...)

```typescript
getContact(options: GetContactOptions) => Promise<GetContactResult>
```

| Param         | Type                                                            |
| ------------- | --------------------------------------------------------------- |
| **`options`** | <code><a href="#getcontactoptions">GetContactOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#getcontactresult">GetContactResult</a>&gt;</code>

--------------------


### getContacts(...)

```typescript
getContacts(options: GetContactsOptions) => Promise<GetContactsResult>
```

| Param         | Type                                                              |
| ------------- | ----------------------------------------------------------------- |
| **`options`** | <code><a href="#getcontactsoptions">GetContactsOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#getcontactsresult">GetContactsResult</a>&gt;</code>

--------------------


### createContact(...)

```typescript
createContact(options: CreateContactOptions) => Promise<CreateContactResult>
```

| Param         | Type                                                                  |
| ------------- | --------------------------------------------------------------------- |
| **`options`** | <code><a href="#createcontactoptions">CreateContactOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#createcontactresult">CreateContactResult</a>&gt;</code>

--------------------


### deleteContact(...)

```typescript
deleteContact(options: DeleteContactOptions) => Promise<void>
```

| Param         | Type                                                                  |
| ------------- | --------------------------------------------------------------------- |
| **`options`** | <code><a href="#deletecontactoptions">DeleteContactOptions</a></code> |

--------------------


### pickContact(...)

```typescript
pickContact(options: PickContactOptions) => Promise<void>
```

| Param         | Type                                                              |
| ------------- | ----------------------------------------------------------------- |
| **`options`** | <code><a href="#pickcontactoptions">PickContactOptions</a></code> |

--------------------


### Interfaces


#### PermissionStatus

| Prop           | Type                                                        |
| -------------- | ----------------------------------------------------------- |
| **`contacts`** | <code><a href="#permissionstate">PermissionState</a></code> |


#### GetContactResult

| Prop          | Type                                                      |
| ------------- | --------------------------------------------------------- |
| **`contact`** | <code><a href="#contactpayload">ContactPayload</a></code> |


#### ContactPayload

| Prop                  | Type                                                                | Description                          |
| --------------------- | ------------------------------------------------------------------- | ------------------------------------ |
| **`contactId`**       | <code>string</code>                                                 |                                      |
| **`name`**            | <code><a href="#namepayload">NamePayload</a></code>                 | Object holding the name data         |
| **`organization`**    | <code><a href="#organizationpayload">OrganizationPayload</a></code> | Object holding the organization data |
| **`birthday`**        | <code><a href="#birthdaypayload">BirthdayPayload</a> \| null</code> | Birthday                             |
| **`note`**            | <code>string \| null</code>                                         | Note                                 |
| **`phones`**          | <code>PhonePayload[]</code>                                         | Phones                               |
| **`emails`**          | <code>EmailPayload[]</code>                                         | Emails                               |
| **`urls`**            | <code>(string \| null)[]</code>                                     | URLs                                 |
| **`postalAddresses`** | <code>PostalAddressPayload[]</code>                                 | Postal Addresses                     |
| **`image`**           | <code><a href="#imagepayload">ImagePayload</a></code>               | Image                                |


#### NamePayload

| Prop          | Type                        |
| ------------- | --------------------------- |
| **`display`** | <code>string \| null</code> |
| **`given`**   | <code>string \| null</code> |
| **`middle`**  | <code>string \| null</code> |
| **`family`**  | <code>string \| null</code> |
| **`prefix`**  | <code>string \| null</code> |
| **`suffix`**  | <code>string \| null</code> |


#### OrganizationPayload

| Prop             | Type                        |
| ---------------- | --------------------------- |
| **`company`**    | <code>string \| null</code> |
| **`jobTitle`**   | <code>string \| null</code> |
| **`department`** | <code>string \| null</code> |


#### BirthdayPayload

| Prop        | Type                        |
| ----------- | --------------------------- |
| **`day`**   | <code>number \| null</code> |
| **`month`** | <code>number \| null</code> |
| **`year`**  | <code>number \| null</code> |


#### PhonePayload

| Prop            | Type                                            |
| --------------- | ----------------------------------------------- |
| **`type`**      | <code><a href="#phonetype">PhoneType</a></code> |
| **`label`**     | <code>string \| null</code>                     |
| **`isPrimary`** | <code>boolean \| null</code>                    |
| **`number`**    | <code>string \| null</code>                     |


#### EmailPayload

| Prop            | Type                                            |
| --------------- | ----------------------------------------------- |
| **`type`**      | <code><a href="#emailtype">EmailType</a></code> |
| **`label`**     | <code>string \| null</code>                     |
| **`isPrimary`** | <code>boolean \| null</code>                    |
| **`address`**   | <code>string \| null</code>                     |


#### PostalAddressPayload

| Prop               | Type                                                            |
| ------------------ | --------------------------------------------------------------- |
| **`type`**         | <code><a href="#postaladdresstype">PostalAddressType</a></code> |
| **`label`**        | <code>string \| null</code>                                     |
| **`isPrimary`**    | <code>boolean \| null</code>                                    |
| **`street`**       | <code>string \| null</code>                                     |
| **`neighborhood`** | <code>string \| null</code>                                     |
| **`city`**         | <code>string \| null</code>                                     |
| **`region`**       | <code>string \| null</code>                                     |
| **`postcode`**     | <code>string \| null</code>                                     |
| **`country`**      | <code>string \| null</code>                                     |


#### ImagePayload

| Prop               | Type                        |
| ------------------ | --------------------------- |
| **`base64String`** | <code>string \| null</code> |


#### GetContactOptions

| Prop             | Type                                              |
| ---------------- | ------------------------------------------------- |
| **`contactId`**  | <code>string</code>                               |
| **`projection`** | <code><a href="#projection">Projection</a></code> |


#### Projection

| Prop                  | Type                 | Description                                                              | Default            |
| --------------------- | -------------------- | ------------------------------------------------------------------------ | ------------------ |
| **`name`**            | <code>boolean</code> |                                                                          | <code>false</code> |
| **`organization`**    | <code>boolean</code> |                                                                          | <code>false</code> |
| **`birthday`**        | <code>boolean</code> |                                                                          | <code>false</code> |
| **`note`**            | <code>boolean</code> |                                                                          | <code>false</code> |
| **`phones`**          | <code>boolean</code> |                                                                          | <code>false</code> |
| **`emails`**          | <code>boolean</code> |                                                                          | <code>false</code> |
| **`urls`**            | <code>boolean</code> |                                                                          | <code>false</code> |
| **`postalAddresses`** | <code>boolean</code> |                                                                          | <code>false</code> |
| **`image`**           | <code>boolean</code> | Be careful! This can potentially slow down your query by a large factor. | <code>false</code> |


#### GetContactsResult

| Prop           | Type                          |
| -------------- | ----------------------------- |
| **`contacts`** | <code>ContactPayload[]</code> |


#### GetContactsOptions

| Prop             | Type                                              |
| ---------------- | ------------------------------------------------- |
| **`projection`** | <code><a href="#projection">Projection</a></code> |


#### CreateContactResult

| Prop            | Type                |
| --------------- | ------------------- |
| **`contactId`** | <code>string</code> |


#### CreateContactOptions

| Prop          | Type                                                  |
| ------------- | ----------------------------------------------------- |
| **`contact`** | <code><a href="#contactinput">ContactInput</a></code> |


#### ContactInput

| Prop                  | Type                                                            | Description                          |
| --------------------- | --------------------------------------------------------------- | ------------------------------------ |
| **`name`**            | <code><a href="#nameinput">NameInput</a></code>                 | Object holding the name data         |
| **`organization`**    | <code><a href="#organizationinput">OrganizationInput</a></code> | Object holding the organization data |
| **`birthday`**        | <code><a href="#birthdayinput">BirthdayInput</a> \| null</code> | Birthday                             |
| **`note`**            | <code>string \| null</code>                                     | Note                                 |
| **`phones`**          | <code>PhoneInput[]</code>                                       | Phones                               |
| **`emails`**          | <code>EmailInput[]</code>                                       | Emails                               |
| **`urls`**            | <code>string[]</code>                                           | URLs                                 |
| **`postalAddresses`** | <code>PostalAddressInput[]</code>                               | Postal Addresses                     |


#### NameInput

| Prop         | Type                        |
| ------------ | --------------------------- |
| **`given`**  | <code>string \| null</code> |
| **`middle`** | <code>string \| null</code> |
| **`family`** | <code>string \| null</code> |
| **`prefix`** | <code>string \| null</code> |
| **`suffix`** | <code>string \| null</code> |


#### OrganizationInput

| Prop             | Type                        |
| ---------------- | --------------------------- |
| **`company`**    | <code>string \| null</code> |
| **`jobTitle`**   | <code>string \| null</code> |
| **`department`** | <code>string \| null</code> |


#### BirthdayInput

| Prop        | Type                |
| ----------- | ------------------- |
| **`day`**   | <code>number</code> |
| **`month`** | <code>number</code> |
| **`year`**  | <code>number</code> |


#### PhoneInput

| Prop            | Type                                            |
| --------------- | ----------------------------------------------- |
| **`type`**      | <code><a href="#phonetype">PhoneType</a></code> |
| **`label`**     | <code>string \| null</code>                     |
| **`isPrimary`** | <code>boolean</code>                            |
| **`number`**    | <code>string \| null</code>                     |


#### EmailInput

| Prop            | Type                                            |
| --------------- | ----------------------------------------------- |
| **`type`**      | <code><a href="#emailtype">EmailType</a></code> |
| **`label`**     | <code>string \| null</code>                     |
| **`isPrimary`** | <code>boolean</code>                            |
| **`address`**   | <code>string \| null</code>                     |


#### PostalAddressInput

| Prop               | Type                                                            |
| ------------------ | --------------------------------------------------------------- |
| **`type`**         | <code><a href="#postaladdresstype">PostalAddressType</a></code> |
| **`label`**        | <code>string \| null</code>                                     |
| **`isPrimary`**    | <code>boolean</code>                                            |
| **`street`**       | <code>string \| null</code>                                     |
| **`neighborhood`** | <code>string \| null</code>                                     |
| **`city`**         | <code>string \| null</code>                                     |
| **`region`**       | <code>string \| null</code>                                     |
| **`postcode`**     | <code>string \| null</code>                                     |
| **`country`**      | <code>string \| null</code>                                     |


#### DeleteContactOptions

| Prop            | Type                |
| --------------- | ------------------- |
| **`contactId`** | <code>string</code> |


#### PickContactOptions

| Prop             | Type                                              |
| ---------------- | ------------------------------------------------- |
| **`projection`** | <code><a href="#projection">Projection</a></code> |


### Type Aliases


#### PermissionState

<code>'prompt' | 'prompt-with-rationale' | 'granted' | 'denied'</code>


### Enums


#### PhoneType

| Members           | Value                       |
| ----------------- | --------------------------- |
| **`Home`**        | <code>'home'</code>         |
| **`Work`**        | <code>'work'</code>         |
| **`Other`**       | <code>'other'</code>        |
| **`Custom`**      | <code>'custom'</code>       |
| **`Mobile`**      | <code>'mobile'</code>       |
| **`FaxWork`**     | <code>'fax_work'</code>     |
| **`FaxHome`**     | <code>'fax_home'</code>     |
| **`Pager`**       | <code>'pager'</code>        |
| **`Callback`**    | <code>'callback'</code>     |
| **`Car`**         | <code>'car'</code>          |
| **`CompanyMain`** | <code>'company_main'</code> |
| **`Isdn`**        | <code>'isdn'</code>         |
| **`Main`**        | <code>'main'</code>         |
| **`OtherFax`**    | <code>'other_fax'</code>    |
| **`Radio`**       | <code>'radio'</code>        |
| **`Telex`**       | <code>'telex'</code>        |
| **`TtyTdd`**      | <code>'tty_tdd'</code>      |
| **`WorkMobile`**  | <code>'work_mobile'</code>  |
| **`WorkPager`**   | <code>'work_pager'</code>   |
| **`Assistant`**   | <code>'assistant'</code>    |
| **`Mms`**         | <code>'mms'</code>          |


#### EmailType

| Members      | Value                 |
| ------------ | --------------------- |
| **`Home`**   | <code>'home'</code>   |
| **`Work`**   | <code>'work'</code>   |
| **`Other`**  | <code>'other'</code>  |
| **`Custom`** | <code>'custom'</code> |
| **`Mobile`** | <code>'mobile'</code> |


#### PostalAddressType

| Members      | Value                 |
| ------------ | --------------------- |
| **`Home`**   | <code>'home'</code>   |
| **`Work`**   | <code>'work'</code>   |
| **`Other`**  | <code>'other'</code>  |
| **`Custom`** | <code>'custom'</code> |

</docgen-api>
