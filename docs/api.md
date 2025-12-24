# API Reference 🔌

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### checkPermissions()

```typescript
checkPermissions() => Promise<PermissionStatus>
```

**Returns:** <code>Promise&lt;[PermissionStatus](#permissionstatus)&gt;</code>

--------------------


### requestPermissions()

```typescript
requestPermissions() => Promise<PermissionStatus>
```

**Returns:** <code>Promise&lt;[PermissionStatus](#permissionstatus)&gt;</code>

--------------------


### getContact(...)

```typescript
getContact(options: GetContactOptions) => Promise<GetContactResult>
```

| Param         | Type                                                 |
| ------------- | ---------------------------------------------------- |
| **`options`** | <code>[GetContactOptions](#getcontactoptions)</code> |

**Returns:** <code>Promise&lt;[GetContactResult](#getcontactresult)&gt;</code>

--------------------


### getContacts(...)

```typescript
getContacts(options: GetContactsOptions) => Promise<GetContactsResult>
```

| Param         | Type                                                   |
| ------------- | ------------------------------------------------------ |
| **`options`** | <code>[GetContactsOptions](#getcontactsoptions)</code> |

**Returns:** <code>Promise&lt;[GetContactsResult](#getcontactsresult)&gt;</code>

--------------------


### createContact(...)

```typescript
createContact(options: CreateContactOptions) => Promise<CreateContactResult>
```

| Param         | Type                                                       |
| ------------- | ---------------------------------------------------------- |
| **`options`** | <code>[CreateContactOptions](#createcontactoptions)</code> |

**Returns:** <code>Promise&lt;[CreateContactResult](#createcontactresult)&gt;</code>

--------------------


### deleteContact(...)

```typescript
deleteContact(options: DeleteContactOptions) => Promise<void>
```

| Param         | Type                                                       |
| ------------- | ---------------------------------------------------------- |
| **`options`** | <code>[DeleteContactOptions](#deletecontactoptions)</code> |

--------------------


### pickContact(...)

```typescript
pickContact(options: PickContactOptions) => Promise<PickContactResult>
```

| Param         | Type                                                   |
| ------------- | ------------------------------------------------------ |
| **`options`** | <code>[PickContactOptions](#pickcontactoptions)</code> |

**Returns:** <code>Promise&lt;[PickContactResult](#pickcontactresult)&gt;</code>

--------------------


### Interfaces


#### PermissionStatus

| Prop           | Type                                                          |
| -------------- | ------------------------------------------------------------- |
| **`contacts`** | <code>[PermissionState](#permissionstate) \| 'limited'</code> |


#### GetContactResult

| Prop          | Type                                           |
| ------------- | ---------------------------------------------- |
| **`contact`** | <code>[ContactPayload](#contactpayload)</code> |


#### ContactPayload

| Prop                  | Type                                                     | Description                          |
| --------------------- | -------------------------------------------------------- | ------------------------------------ |
| **`contactId`**       | <code>string</code>                                      |                                      |
| **`name`**            | <code>[NamePayload](#namepayload)</code>                 | Object holding the name data         |
| **`organization`**    | <code>[OrganizationPayload](#organizationpayload)</code> | Object holding the organization data |
| **`birthday`**        | <code>[BirthdayPayload](#birthdaypayload) \| null</code> | Birthday                             |
| **`note`**            | <code>string \| null</code>                              | Note                                 |
| **`phones`**          | <code>PhonePayload[]</code>                              | Phones                               |
| **`emails`**          | <code>EmailPayload[]</code>                              | Emails                               |
| **`urls`**            | <code>(string \| null)[]</code>                          | URLs                                 |
| **`postalAddresses`** | <code>PostalAddressPayload[]</code>                      | Postal Addresses                     |
| **`image`**           | <code>[ImagePayload](#imagepayload)</code>               | Image                                |


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

| Prop            | Type                                 |
| --------------- | ------------------------------------ |
| **`type`**      | <code>[PhoneType](#phonetype)</code> |
| **`label`**     | <code>string \| null</code>          |
| **`isPrimary`** | <code>boolean \| null</code>         |
| **`number`**    | <code>string \| null</code>          |


#### EmailPayload

| Prop            | Type                                 |
| --------------- | ------------------------------------ |
| **`type`**      | <code>[EmailType](#emailtype)</code> |
| **`label`**     | <code>string \| null</code>          |
| **`isPrimary`** | <code>boolean \| null</code>         |
| **`address`**   | <code>string \| null</code>          |


#### PostalAddressPayload

| Prop               | Type                                                 |
| ------------------ | ---------------------------------------------------- |
| **`type`**         | <code>[PostalAddressType](#postaladdresstype)</code> |
| **`label`**        | <code>string \| null</code>                          |
| **`isPrimary`**    | <code>boolean \| null</code>                         |
| **`street`**       | <code>string \| null</code>                          |
| **`neighborhood`** | <code>string \| null</code>                          |
| **`city`**         | <code>string \| null</code>                          |
| **`region`**       | <code>string \| null</code>                          |
| **`postcode`**     | <code>string \| null</code>                          |
| **`country`**      | <code>string \| null</code>                          |


#### ImagePayload

| Prop               | Type                        |
| ------------------ | --------------------------- |
| **`base64String`** | <code>string \| null</code> |


#### GetContactOptions

| Prop             | Type                                   |
| ---------------- | -------------------------------------- |
| **`contactId`**  | <code>string</code>                    |
| **`projection`** | <code>[Projection](#projection)</code> |


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

| Prop             | Type                                   |
| ---------------- | -------------------------------------- |
| **`projection`** | <code>[Projection](#projection)</code> |


#### CreateContactResult

| Prop            | Type                |
| --------------- | ------------------- |
| **`contactId`** | <code>string</code> |


#### CreateContactOptions

| Prop          | Type                                       |
| ------------- | ------------------------------------------ |
| **`contact`** | <code>[ContactInput](#contactinput)</code> |


#### ContactInput

| Prop                  | Type                                                 | Description                          |
| --------------------- | ---------------------------------------------------- | ------------------------------------ |
| **`name`**            | <code>[NameInput](#nameinput)</code>                 | Object holding the name data         |
| **`organization`**    | <code>[OrganizationInput](#organizationinput)</code> | Object holding the organization data |
| **`birthday`**        | <code>[BirthdayInput](#birthdayinput) \| null</code> | Birthday                             |
| **`note`**            | <code>string \| null</code>                          | Note                                 |
| **`phones`**          | <code>PhoneInput[]</code>                            | Phones                               |
| **`emails`**          | <code>EmailInput[]</code>                            | Emails                               |
| **`urls`**            | <code>string[]</code>                                | URLs                                 |
| **`postalAddresses`** | <code>PostalAddressInput[]</code>                    | Postal Addresses                     |
| **`image`**           | <code>[ImageInput](#imageinput) \| null</code>       | Image                                |


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

| Prop            | Type                                 |
| --------------- | ------------------------------------ |
| **`type`**      | <code>[PhoneType](#phonetype)</code> |
| **`label`**     | <code>string \| null</code>          |
| **`isPrimary`** | <code>boolean</code>                 |
| **`number`**    | <code>string \| null</code>          |


#### EmailInput

| Prop            | Type                                 |
| --------------- | ------------------------------------ |
| **`type`**      | <code>[EmailType](#emailtype)</code> |
| **`label`**     | <code>string \| null</code>          |
| **`isPrimary`** | <code>boolean</code>                 |
| **`address`**   | <code>string \| null</code>          |


#### PostalAddressInput

| Prop               | Type                                                 |
| ------------------ | ---------------------------------------------------- |
| **`type`**         | <code>[PostalAddressType](#postaladdresstype)</code> |
| **`label`**        | <code>string \| null</code>                          |
| **`isPrimary`**    | <code>boolean</code>                                 |
| **`street`**       | <code>string \| null</code>                          |
| **`neighborhood`** | <code>string \| null</code>                          |
| **`city`**         | <code>string \| null</code>                          |
| **`region`**       | <code>string \| null</code>                          |
| **`postcode`**     | <code>string \| null</code>                          |
| **`country`**      | <code>string \| null</code>                          |


#### ImageInput

| Prop               | Type                        | Description           |
| ------------------ | --------------------------- | --------------------- |
| **`base64String`** | <code>string \| null</code> | Base64 encoded image. |


#### DeleteContactOptions

| Prop            | Type                |
| --------------- | ------------------- |
| **`contactId`** | <code>string</code> |


#### PickContactResult

| Prop          | Type                                           |
| ------------- | ---------------------------------------------- |
| **`contact`** | <code>[ContactPayload](#contactpayload)</code> |


#### PickContactOptions

| Prop             | Type                                   |
| ---------------- | -------------------------------------- |
| **`projection`** | <code>[Projection](#projection)</code> |


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
