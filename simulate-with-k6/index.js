import {Rate} from 'k6/metrics';
import http from 'k6/http';
import {sleep} from "k6";
// import * as faker from 'faker/locale/en_US';
import faker from "https://raw.githubusercontent.com/Marak/faker.js/9c65e5dd4902dbdf12088c36c098a9d7958afe09/dist/faker.min.js";

// sleep from max to 1% of max
const MAX_SLEEP_AMOUNT = 1

const ids = []
// noinspection PointlessArithmeticExpressionJS
ids.length = 1000
for (let i = 0; i < ids.length; i++) {
    ids[i] = faker.random.uuid()
}

const createAdReq = () => ({
    "messageType": "CreateAdRequest",
    "requestId": ids[faker.random.number({min: 0, max: 1000})],
    "createAd": {
        "title": faker.name.jobTitle(),
        "description": faker.name.jobDescriptor(),
        "ownerId": "9435",
        "visibility": "public",
        "dealSide": "demand",
        "product": {
            "productType": "AdProductBolt",
            "threadPitch": faker.random.number({min: 0, max: 1000, precision: 0.1}),
            "diameter": faker.random.number({min: 0, max: 1000, precision: 0.1}),
            "lengh": faker.random.number({min: 0, max: 1000, precision: 0.1})
        }
    },
    "debug": {
        "mode": "test"
    }
})

const readAdReq = () => ({
    "messageType": "ReadAdRequest",
    "requestId": ids[faker.random.number({min: 0, max: 1000})],
    "readAdId": ids[faker.random.number({min: 0, max: 1000})],
    "debug": {
        "mode": "test"
    }
})

const updateAdReq = () => ({
    "messageType": "UpdateAdRequest",
    "requestId": ids[faker.random.number({min: 0, max: 1000})],
    "createAd": {
        "title": faker.name.jobTitle(),
        "description": faker.name.jobDescriptor(),
        "ownerId": "9435",
        "visibility": "ownerOnly",
        "dealSide": "proposal",
        "product": {
            "productType": "AdProductBolt"
        },
        "id": ids[faker.random.number({min: 0, max: 1000})]
    },
    "debug": {
        "mode": "test"
    }
})

const searchAdReq = () => ({
    "messageType": "SearchAdRequest",
    "requestId": ids[faker.random.number({min: 0, max: 1000})],
    "page": {
        "size": 3,
        "lastId": ids[faker.random.number({min: 0, max: 1000})]
    }
})

const offersAdReq = () => ({
    "messageType": "OffersAdRequest",
    "requestId": ids[faker.random.number({min: 0, max: 1000})],
    "page": {
        "size": 2
    },
    "deleteAdId": ids[faker.random.number({min: 0, max: 1000})],
    "debug": {
        "mode": "test"
    }
})

const deleteAdReq = () => ({
    "messageType": "DeleteAdRequest",
    "requestId": null,
    "deleteAdId": ids[faker.random.number({min: 0, max: 1000})],
    "debug": {
        "mode": "test"
    }
})

let authKey = __ENV["mp_auth_token"]
let baseUrl = __ENV["mp_server_url"]

if (!authKey) {
    authKey = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhZC11c2VycyIsImlzcyI6Ik90dXNLb3RsaW4iLCJncm91cHMiOlsiVEVTVCJdLCJpZCI6IjAwMDAwMDAwLTAwMDAtMDAwMC0wMDAwLTAwMDAwMDAwMDAwMSIsImV4cCI6MjA4Mjc1ODQwMH0.gaxJtUCgdC00r05fNHHHORh45v_ZnvErGsLZgoMuhUY"
    // throw Error("Missing auth key's environment variable <mp_auth_token>")
}
if (!baseUrl) {
    baseUrl = "http://127.0.0.1:8080"
    // throw Error("Missing testing url environment variable <full_line_server_auth_key>")
}

const headers = {
    'User-Agent': 'k6',
    'Content-Type': 'application/json',
    "Authorization": 'Bearer ' + authKey,
};


const endpoints = {
    create: `${baseUrl}/ad/create`,
    read: `${baseUrl}/ad/read`,
    update: `${baseUrl}/ad/update`,
    search: `${baseUrl}/ad/search`,
    offers: `${baseUrl}/ad/offers`,
    delete: `${baseUrl}/ad/delete`,
};

const failRate = new Rate('failed form fetches');

const createForm = () => {
    const res = http.post(endpoints.create, JSON.stringify(createAdReq()), {headers});
    failRate.add(res.status !== 200);
}

const readForm = () => {
    const res = http.post(endpoints.read, JSON.stringify(readAdReq()), {headers});
    failRate.add(res.status !== 200);
}

const updateForm = () => {
    const res = http.post(endpoints.update, JSON.stringify(updateAdReq()), {headers});
    failRate.add(res.status !== 200);
}

const searchForm = () => {
    const res = http.post(endpoints.search, JSON.stringify(searchAdReq()), {headers});
    failRate.add(res.status !== 200);
}

const offersForm = () => {
    const res = http.post(endpoints.offers, JSON.stringify(offersAdReq()), {headers});
    failRate.add(res.status !== 200);
}

const deleteForm = () => {
    const res = http.post(endpoints.delete, JSON.stringify(deleteAdReq()), {headers});
    failRate.add(res.status !== 200);
}

export default function () {
    createForm()
    sleep(MAX_SLEEP_AMOUNT * faker.random.number({min: 0, max: 1, precision: 0.01}))
    readForm()
    sleep(MAX_SLEEP_AMOUNT * faker.random.number({min: 0, max: 1, precision: 0.01}))
    updateForm()
    sleep(MAX_SLEEP_AMOUNT * faker.random.number({min: 0, max: 1, precision: 0.01}))
    searchForm()
    sleep(MAX_SLEEP_AMOUNT * faker.random.number({min: 0, max: 1, precision: 0.01}))
    offersForm()
    sleep(MAX_SLEEP_AMOUNT * faker.random.number({min: 0, max: 1, precision: 0.01}))
    deleteForm()
}
