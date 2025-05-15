'use strict';

var core = require('@capacitor/core');

exports.PhoneType = void 0;
(function (PhoneType) {
    // Home / Work
    PhoneType["Home"] = "home";
    PhoneType["Work"] = "work";
    // Other / Custom
    PhoneType["Other"] = "other";
    PhoneType["Custom"] = "custom";
    // Phone specific
    PhoneType["Mobile"] = "mobile";
    PhoneType["FaxWork"] = "fax_work";
    PhoneType["FaxHome"] = "fax_home";
    PhoneType["Pager"] = "pager";
    PhoneType["Callback"] = "callback";
    PhoneType["Car"] = "car";
    PhoneType["CompanyMain"] = "company_main";
    PhoneType["Isdn"] = "isdn";
    PhoneType["Main"] = "main";
    PhoneType["OtherFax"] = "other_fax";
    PhoneType["Radio"] = "radio";
    PhoneType["Telex"] = "telex";
    PhoneType["TtyTdd"] = "tty_tdd";
    PhoneType["WorkMobile"] = "work_mobile";
    PhoneType["WorkPager"] = "work_pager";
    PhoneType["Assistant"] = "assistant";
    PhoneType["Mms"] = "mms";
})(exports.PhoneType || (exports.PhoneType = {}));
exports.EmailType = void 0;
(function (EmailType) {
    // Home / Work
    EmailType["Home"] = "home";
    EmailType["Work"] = "work";
    // Other / Custom
    EmailType["Other"] = "other";
    EmailType["Custom"] = "custom";
    // Email specific
    EmailType["Mobile"] = "mobile";
})(exports.EmailType || (exports.EmailType = {}));
exports.PostalAddressType = void 0;
(function (PostalAddressType) {
    // Home / Work
    PostalAddressType["Home"] = "home";
    PostalAddressType["Work"] = "work";
    // Other / Custom
    PostalAddressType["Other"] = "other";
    PostalAddressType["Custom"] = "custom";
})(exports.PostalAddressType || (exports.PostalAddressType = {}));

const Contacts = core.registerPlugin('Contacts', {
    web: () => Promise.resolve().then(function () { return web; }).then((m) => new m.ContactsWeb()),
});

class ContactsWeb extends core.WebPlugin {
    async checkPermissions() {
        throw this.unimplemented('Not implemented on web.');
    }
    async requestPermissions() {
        throw this.unimplemented('Not implemented on web.');
    }
    async getContact() {
        throw this.unimplemented('Not implemented on web.');
    }
    async getContacts() {
        throw this.unimplemented('Not implemented on web.');
    }
    async createContact() {
        throw this.unimplemented('Not implemented on web.');
    }
    async deleteContact() {
        throw this.unimplemented('Not implemented on web.');
    }
    async pickContact() {
        throw this.unimplemented('Not implemented on web.');
    }
}

var web = /*#__PURE__*/Object.freeze({
    __proto__: null,
    ContactsWeb: ContactsWeb
});

exports.Contacts = Contacts;
//# sourceMappingURL=plugin.cjs.js.map
