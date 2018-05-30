/**
 * 
 * REST Request to User Management MicroService
 * 
 */

function getAdmins(token) {
    return $.ajax({
        url: 'http://localhost:8081/user-management/admins?tokenId=' + token,
        method: "GET"
    });
}

function loginAdmin(token, admin) {
    return $.ajax({
        url: 'http://localhost:8081/user-management/admins?tokenId=' + token,
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(admin)
    });
}

function deleteAdmin(token, adminId) {
    return $.ajax({
        url: 'http://localhost:8081/user-management/admins/' + adminId + '?tokenId=' + token,
        method: 'DELETE'
    });
}

function isAuthorized(token, rol) {
    return $.ajax({
        url: 'http://localhost:8081/user-management/authorizations?tokenId=' + token + '&rol=' + rol,
        method: "GET"
    })
}

function signin(username, password) {
    return $.ajax({
        url: 'http://localhost:8081/user-management/signin?username=' + username + '&password=' + password,
        method: 'GET'
    });
}

function signout(token) {
    return $.ajax({
        url: 'http://localhost:8081/user-management/signout?tokenId=' + token,
        method: 'POST'
    });
}

function getUsers(token) {
    return $.ajax({
        url: 'http://localhost:8081/user-management/users?tokenId=' + token,
        method: "GET"
    });
}

function updateUser(token, user) {
    return $.ajax({
        url: 'http://localhost:8081/user-management/users?tokenId=' + token,
        method: "POST",
        contentType: 'application/json',
        data: JSON.stringify(user)
    });
}

function loginUser(user) {
    return $.ajax({
        url: 'http://localhost:8081/user-management/users',
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(user)
    })
}

function existsUser(token, username) { //No usado
    return $.ajax({
        url: 'http://localhost:8081/user-management/users/exists?tokenId=' + token + '&username=' + username,
        method: 'GET'
    });
}

function lockUser(token, userId) {
    return $.ajax({
        url: 'http://localhost:8081/user-management/users/' + userId + '/lock?tokenId=' + token,
        method: 'POST'
    });
}

function deleteUser(token, password) {
    return $.ajax({
        url: 'http://localhost:8081/user-management/users/' + token + '?password=' + password,
        method: 'DELETE'
    });
}

function getUser(token) {
    return $.ajax({
        url: 'http://localhost:8081/user-management/users/' + token,
        method: 'GET'
    });
}

function updatePassword(token, oldPassword, newPassword) {
    return $.ajax({
        url: 'http://localhost:8081/user-management/users/' + token + '/password?oldPassword=' + oldPassword + '&newPassword=' + newPassword,
        method: 'POST'
    })
}

/**
 * 
 * REST Request to Currency Exchange MicroService
 * 
 */

function getCurrency() {
    return $.ajax({
        url: 'http://localhost:8082/currency-exchange/currency',
        method: "GET"
    })
}

function getRatioByDay(token, currency, day) {
    return $.ajax({
        url: 'http://localhost:8082/currency-exchange/ratio/' + day + '?tokenId=' + token + '&baseCurrencyId=' + currency,
        method: "GET"
    })
}

function calculatePrice(from, to, quantity, day) {
    return $.ajax({
        url: 'http://localhost:8082/currency-exchange/ratio/' + day + '/amount?from=' + from + '&to=' + to + '&quantity=' + quantity,
        method: "GET"
    })
}

/**
 * 
 * REST Request to Purchase MicroService
 * 
 */

function getAds(from, to) {
    return $.ajax({
        url: 'http://localhost:8083/purchase/ads',
        method: "GET",
        data: {
            from: from,
            to: to
        }
    });
}

function updateAd(token, ad) {
    return $.ajax({
        url: 'http://localhost:8083/purchase/ads?tokenId=' + token,
        method: "POST",
        contentType: 'application/json',
        data: JSON.stringify(ad)
    });
}

function createAd(token, ad) {
    return $.ajax({
        url: 'http://localhost:8083/purchase/ads?tokenId=' + token,
        method: "PUT",
        contentType: 'application/json',
        data: JSON.stringify(ad)
    });
}

function getAdsBySeller(token) {
    return $.ajax({
        url: 'http://localhost:8083/purchase/ads/seller?tokenId=' + token,
        method: 'GET'
    });
}

function deleteAd(token, adId) {
    return $.ajax({
        url: 'http://localhost:8083/purchase/ads/' + adId + '?tokenId=' + token,
        method: 'DELETE'
    });
}

function getAd(adId) {
    return $.ajax({
        url: 'http://localhost:8083/purchase/ads/' + adId,
        method: "GET"
    });
}

function createPurchaseRequest(token, adId) {
    return $.ajax({
        url: 'http://localhost:8083/purchase/ads/' + adId + '/buy?tokenId=' + token,
        method: "PUT"
    });
}

function getPurchaseRequestsAsApplicant(token) {
    return $.ajax({
        url: 'http://localhost:8083/purchase/requests/applicant?tokenId=' + token,
        method: "GET"
    });
}

function getPurchaseRequestsAsSeller(token) {
    return $.ajax({
        url: 'http://localhost:8083/purchase/requests/seller?tokenId=' + token,
        method: "GET"
    });
}

function deletePurchaseRequest(token, purchaseRequestId) {
    return $.ajax({
        url: 'http://localhost:8083/purchase/requests/' + purchaseRequestId + '?tokenId=' + token,
        method: 'DELETE'
    });
}

function sellAd(token, requestId) {
    return $.ajax({
        url: 'http://localhost:8083/purchase/requests/' + requestId + '/sell?tokenId=' + token,
        method: "POST"
    });
}

/**
 * 
 * REST Request to Communication MicroService
 * 
 */

function getConversations(token) {
    return $.ajax({
        url: 'http://localhost:8084/communication/conversations?tokenId=' + token,
        method: "GET"
    });
}

function getMessages(token, receiver) {
    return $.ajax({
        url: 'http://localhost:8084/communication/messages?tokenId=' + token + '&receiver=' + receiver,
        method: "GET"
    });
}

function createMessage(token, message) {
    return $.ajax({
        url: 'http://localhost:8084/communication/messages?tokenId=' + token,
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(message)
    });
}

function createValuation(token, valuation) {
    return $.ajax({
        url: 'http://localhost:8084/communication/valuations?tokenId=' + token,
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(valuation)
    });
}

function getValuationsByEvaluated(token, evaluated) {
    return $.ajax({
        url: 'http://localhost:8084/communication/valuations/' + evaluated + '?tokenId=' + token,
        method: "GET"
    });
}
