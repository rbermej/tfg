// TODO Extraer a otro documento INICIO

function getToday() {
    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth() + 1;
    var yyyy = today.getFullYear();
    if (dd < 10) dd = '0' + dd;
    if (mm < 10) mm = "0" + mm;
    return yyyy + '-' + mm + '-' + dd;
}

function getLocalToken() {
    return localStorage.getItem('token');
}

function getLocalRol() {
    return localStorage.getItem('rol');
}

function getLocalUsername() {
    return localStorage.getItem('username');
}

function setLocalData(user, username) {
    localStorage.setItem('token', user.token);
    localStorage.setItem('rol', user.rol);
    localStorage.setItem('username', username);
}

function removeLocalData() {
    localStorage.removeItem('token');
    localStorage.removeItem('rol');
    localStorage.removeItem('username');
}

function showError(response) {
    var message = undefined != response ? (jQuery.type(response) === "string" ? response : response.message) : 'ERROR: Server no response.';
    $('#alert').html('<div class="alert alert-danger" role="alert">' + message + '</div>');
}

function getUrlMenu() {
    switch (getLocalRol()) {
        case 'REGISTERED_USER':
            return 'menus/menu-registered-user.html';
        case 'ADMIN':
            return 'menus/menu-admin.html';
        case 'SUPERADMIN':
            return 'menus/menu-superadmin.html';
        default:
            return 'menus/menu-user.html';
    }
}

function getUrlHome() {
    switch (getLocalRol()) {
        case 'REGISTERED_USER':
            return 'list-ads.html';
        case 'ADMIN':
            return 'panel-admin.html';
        case 'SUPERADMIN':
            return 'panel-superadmin.html';
        default:
            return 'list-ads.html';
    }
}

function getHTMLCurrencySign(isoCode) {
    var sign = function () {
        switch (isoCode) {
            case 'EUR': return 'euro';
            case 'GBP': return 'pund';
            case 'JPY': return 'yen';
            case 'USD': return 'dollar';
        }
    }
    return '<i class="fas fa-' + sign() + '-sign fa-3x"></i>';
}

function getHTMLAd(ad) {
    var st = '<div class="col-md-3 mx-auto py-3"><div class="card"><div class="card-header text-center">';
    st += getHTMLCurrencySign(ad.offeredCurrency) + '&nbsp;<i class="fas fa-arrow-right fa-3x"></i>&nbsp;' + getHTMLCurrencySign(ad.demandedCurrency) + '</div>';
    st += '<div class="card-body text-center"><p>' + ad.offeredAmount + ' ' + ad.offeredCurrency + '</p>'
    st += '<p><i class="fas fa-map-marker"></i>&nbsp;' + ad.location + '</p>';
    st += '<p>Seller:&nbsp;<a href="list-valuations.html?user=' + ad.seller + '">' + ad.seller + '</a>&nbsp;';
    st += '<a href="list-messages.html?receiver=' + ad.seller + '"><i class="far fa-comments"></i></a></p>';
    st += '<p>' + ad.demandedAmount + ' ' + ad.demandedCurrency + '&nbsp;<a class="btn btn-success btn-sm btnBuyAd" role="button" href="' + ad.id + '">Buy</a></p></div></div></div>';
    return st;
}

function getHTMLValuation(valuation) {
    var author = getLocalUsername() == valuation.evaluator ? valuation.evaluator : '<a href="list-valuations.html?user=' + valuation.evaluator + '" role="button">' + valuation.evaluator + '</a>';
    var message = valuation.text == null ? '' : '<div class="row"><div class="col messageContent py-1 text-justify">' + valuation.text + '</div></div>';
    return '<div class="valuation container py-1 my-2 border rounded"><div class="row">' +
        '<div class="col autor">' + author + '</div><div class="col date">' + new Date(valuation.date).toUTCString() + '</div></div>' +
        '<div class="row"><div class="col py-1 text-justify">Points: ' + valuation.points + '</div></div>' + message + '</div>';
}

function loadAds(ads) {
    $('#cardsListAds').empty();
    $.each(ads, function (key, ad) {
        $('#cardsListAds').append(getHTMLAd(ad));
    });
    $('.btnBuyAd')
        .click(function (e) {
            e.preventDefault();
            createPurchaseRequest(getLocalToken(), $(this).attr('href'))
                .done(function () {
                    alert('Purchase request created.');
                })
                .fail(function (jqXHR) {
                    showError(jqXHR.responseJSON);
                })
        });
}

function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;
    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
}

// TODO Extraer a otro documento FIN
$(document).ready(function () {

    //Load menu (depends user rol)
    if ($('#menu').length) {
        $('#menu').load(getUrlMenu() + ' .navbar', function (responseTxt, statusTxt, xhr) {
            if (statusTxt == "success") {
                $('#user-username').text(getLocalUsername() != null ? getLocalUsername() : 'guest');
                //Signout user
                $('#btnSignout')
                    .click(function (e) {
                        e.preventDefault();
                        signout(getLocalToken())
                            .done(function () {
                                removeLocalData();
                                $(location).attr('href', getUrlHome());
                            })
                    });
            }

        });
    }

    //Load currency list (if exist currency id)
    if ($('#login-user').length || $('#update-user').length || $('#historical-ratios').length ||
        $('#calculate-price').length || $('#list-ads').length || $('#form-ad').length) {
        getCurrency()
            .done(function (currencies) {
                $.each(currencies, function (key, currency) {
                    $(".currencies").append('<option value=' + currency.isoCode + '>' + currency.name + '</option>');
                });
                $(".currencies").parent().show();
            })
            .fail(function (jqXHR) {
                $(".currencies").parent().hide();
            });
    }

    //Login Admin
    $('#formLoginAdmin')
        .submit(function (e) {
            e.preventDefault();
            var admin = {
                email: $('#email').val(),
                password: $('#password').val(),
                username: $('#username').val()
            }
            loginAdmin(getLocalToken(), admin)
                .done(function () {
                    alert('Admin created. Accept to go to home page');
                    $(location).attr('href', getUrlHome());
                })
                .fail(function (jqXHR) {
                    showError(jqXHR.responseJSON);
                });

        });

    //Login User
    $('#formLoginUser')
        .submit(function (e) {
            e.preventDefault();
            var password1 = $('#password1').val();
            var password2 = $('#password2').val();
            if (password1 != password2) {
                showError('ERROR: Password aren\'t equals');
            }
            else {
                var user = {
                    defaultCurrency: $('#currencies').val(),
                    email: $('#email').val(),
                    password: password1,
                    username: $('#username').val()
                }
                loginUser(user)
                    .done(function () {
                        alert('User created. Accept to go to sigin page');
                        $(location).attr('href', 'signin.html');
                    })
                    .fail(function (jqXHR) {
                        showError(jqXHR.responseJSON);
                    });
            }
        });

    //Signin user
    $('#formSignin')
        .submit(function (e) {
            e.preventDefault();
            signin($('#username').val(), $('#password').val())
                .done(function (user) {
                    setLocalData(user, $('#username').val());
                    $(location).attr('href', getUrlHome());
                })
                .fail(function (jqXHR) {
                    showError(jqXHR.responseJSON);
                });
        });

    //Filter data on table
    $("#search")
        .on("keyup", function () {
            var value = $(this).val().toLowerCase();
            $("#searchFields tr").filter(function () {
                $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
            });
        });

    //Load user (if exist form update user)
    if ($('#formUpdateUser').length) {
        getUser(getLocalToken())
            .done(function (user) {
                $("#email").val(user.email);
                $("#currencies").val(user.defaultCurrency);
            })
            .fail(function (jqXHR) {
                showError(jqXHR.responseJSON);
            });
    }

    //Update user
    $('#formUpdateUser')
        .submit(function (e) {
            e.preventDefault();
            var user = {
                defaultCurrency: $('#currencies').val(),
                email: $('#email').val(),
                password: $('#password').val()
            }
            updateUser(getLocalToken(), user)
                .done(function () {
                    alert('User updated. Accept to go to home page');
                    $(location).attr('href', getUrlHome());
                })
                .fail(function (jqXHR) {
                    showError(jqXHR.responseJSON);
                });
        });

    //Update password
    $('#formUpdatePassword')
        .submit(function (e) {
            e.preventDefault();
            var password1 = $('#new-password').val();
            var password2 = $('#confirm-password').val();
            if (password1 != password2) {
                showError('ERROR: Password aren\'t equals');
            }
            else {
                updatePassword(getLocalToken(), $('#old-password').val(), password1)
                    .done(function () {
                        alert('Password updated. Accept to go to home page');
                        $(location).attr('href', getUrlHome());
                    })
                    .fail(function (jqXHR) {
                        showError(jqXHR.responseJSON);
                    });
            }
        });

    //Deactivate account
    $('#formDeactivateAccount')
        .submit(function (e) {
            e.preventDefault();
            deleteUser(getLocalToken(), $('#password').val())
                .done(function () {
                    removeLocalData();
                    $(location).attr('href', getUrlHome());
                })
                .fail(function (jqXHR) {
                    showError(jqXHR.responseJSON);
                });
        });

    //Load admins list (if exist panel superadmin)
    if ($('#panel-superadmin').length) {
        getAdmins(getLocalToken())
            .done(function (admins) {
                $.each(admins, function (key, admin) {
                    var tr = '<tr><td>' + admin.username + '</td><td>' + admin.email + '</td>';
                    if (admin.deleted) {
                        tr += '</tr>';
                        $('#tableDeletedAdmins tbody').append(tr);
                    } else {
                        tr += '<td class="actions"><a class="btnDeleteAdmin" href="' + admin.id + '" role="button" title="delete admin"><i class="fas fa-trash-alt"></i></a></td></tr>';
                        $('#tableActivedAdmins tbody').append(tr);
                    }
                });
                $('.btnDeleteAdmin')
                    .click(function (e) {
                        e.preventDefault();
                        var button = $(this);
                        //Delete admin
                        deleteAdmin(getLocalToken(), button.attr('href'))
                            .done(function () {
                                location.reload();
                            })
                            .fail(function (jqXHR) {
                                showError(jqXHR.responseJSON);
                            })
                    });
            })
            .fail(function (jqXHR) {
                showError(jqXHR.responseJSON);
            });
    }

    //Load admins list (if exist panel superadmin)
    if ($('#panel-admin').length) {
        getUsers(getLocalToken())
            .done(function (users) {
                $.each(users, function (key, user) {
                    var class_tr = user.status != 'BLOQUED' ? '' : ' class="table-danger"';
                    var a = user.status == 'BLOQUED' ? '' : '<a class="btnLockUser" href="' + user.id + '" role="button"><i class="fas fa-ban"></i></a>';
                    var tr = '<tr' + class_tr + '><td>' + user.username + '</td><td>' + user.email + '</td><td>' + user.status + '</td><td class="actions">' + a + '</td></tr>';
                    $('#searchFields').append(tr);
                });
                $('.btnLockUser')
                    .click(function (e) {
                        e.preventDefault();
                        var button = $(this);
                        var tr = button.closest('tr');
                        lockUser(getLocalToken(), button.attr('href'))
                            .done(function () {
                                $(tr).children('td:nth-child(3)').text('BLOQUED');
                                $(tr).children('td:nth-child(4)').empty();
                                $(tr).addClass('table-danger');
                            })
                            .fail(function (jqXHR) {
                                showError(jqXHR.responseJSON);
                            })
                    });
            })
            .fail(function (jqXHR) {
                showError(jqXHR.responseJSON);
            });
    }

    //Load deafult day (today)
    if ($('#day').length) {
        var today = getToday();
        document.getElementById("day").max = today;
        $('#day').val(today);
    }

    //Get Ratios
    $('#formHistoricalRatios')
        .submit(function (e) {
            e.preventDefault();
            var currency = $('#currencies').val();
            var day = $('#day').val();
            getRatioByDay(getLocalToken(), currency, day)
                .done(function (ratio) {
                    var st = ratio.baseCurrency.currency.isoCode + ' ' + ratio.baseCurrency.value;
                    $.each(ratio.destinationCurrencies, function (key, currency) {
                        st += '<br>' + currency.currency.isoCode + ' ' + currency.value;
                    });
                    $('#alert').html('<div class="alert alert-primary" role="alert">' + st + '</div>');
                })
                .fail(function (jqXHR) {
                    showError(jqXHR.responseJSON);
                });
        });

    //Calculate price
    $('#formCalculatePrice')
        .submit(function (e) {
            e.preventDefault();
            var from = $('#from').val();
            var to = $('#to').val();
            var quantity = $('#quantity').val();
            var day = $('#day').val();
            calculatePrice(from, to, quantity, day)
                .done(function (amount) {
                    var st = quantity + ' ' + from + ' = ' + amount + ' ' + to;
                    $('#alert').html('<div class="alert alert-primary" role="alert">' + st + '</div>');
                })
                .fail(function (jqXHR) {
                    showError(jqXHR.responseJSON);
                });
        });

    //Load ads list (if exist ads list)
    if ($('#list-ads').length) {
        getAds()
            .done(function (ads) {
                loadAds(ads);
            })
            .fail(function (jqXHR) {
                showError(jqXHR.responseJSON);
            });
    }

    //Filter ads list
    $('#formListAds')
        .submit(function (e) {
            e.preventDefault();
            var from = $('#from').val() != '' ? $('#from').val() : undefined;
            var to = $('#to').val() != '' ? $('#to').val() : undefined;
            getAds(from, to)
                .done(function (ads) {
                    loadAds(ads);
                })
                .fail(function (jqXHR) {
                    showError(jqXHR.responseJSON);
                });
        });

    //Calculate amount on form-ad
    if ($('#form-ad').length) {
        $(".required")
            .on("change", function () {
                var from = $('#from').val();
                var to = $('#to').val();
                var quantity = $('#quantity').val();
                var day = getToday();
                if (from != '' && to != '' && quantity != '') {
                    //Calculate Amount
                    calculatePrice(from, to, quantity, day)
                        .done(function (amount) {
                            $('#amount').val(amount);
                        })
                        .fail(function (jqXHR) {
                            $('#amount').val('');
                        });
                }
                else {
                    $('#amount').val('');
                }
            });
    }

    if ($('#form-ad').length) {
        var adId = getUrlParameter('adId');
        if (adId !== undefined) {
            $('h2').text('Update ad');
            //Load Ad
            getAd(adId)
                .done(function (ad) {
                    $('#pk').val(ad.id);
                    $('#from').val(ad.offeredCurrency);
                    $('#quantity').val(ad.offeredAmount);
                    $('#to').val(ad.demandedCurrency);
                    $('#amount').val(ad.demandedAmount);
                    $('#location').val(ad.location);
                })
                .fail(function (jqXHR) {
                    showError(jqXHR.responseJSON);
                });
        }
        else
            $('h2').text('Create ad');
        //Save Ad
        $('#formAd')
            .submit(function (e) {
                e.preventDefault();
                var ad = {
                    id: $('#pk').val(),
                    offeredCurrency: $('#from').val(),
                    offeredAmount: $('#quantity').val(),
                    demandedCurrency: $('#to').val(),
                    location: $('#location').val()
                }
                if (ad.id === '') {
                    //Create Ad
                    createAd(getLocalToken(), ad)
                        .done(function () {
                            alert('Ad saved. Accept to go to panel seller page');
                            $(location).attr('href', 'panel-seller.html');
                        })
                        .fail(function (jqXHR) {
                            showError(jqXHR.responseJSON);
                        });
                }
                else {
                    //Update Ad
                    updateAd(getLocalToken(), ad)
                        .done(function () {
                            alert('Ad saved. Accept to go to panel seller page');
                            $(location).attr('href', 'panel-seller.html');
                        })
                        .fail(function (jqXHR) {
                            showError(jqXHR.responseJSON);
                        });
                }
            });
    }

    //Load ad list and purchase requests list as sellers (if exist panel seller)
    if ($('#panel-seller').length) {
        getAdsBySeller(getLocalToken())
            .done(function (ads) {
                $.each(ads, function (key, ad) {
                    var tdActions = ad.status == 'SOLD' ? '' : '<a class="btnUpdateAd" href="form-ad.html?adId=' + ad.id + '" role="button"><i class="fas fa-edit"></i></a>&nbsp;' +
                        '&nbsp;<a class="btnDeleteAd" href="' + ad.id + '" role="button"><i class="fas fa-trash"></i></a>';
                    var tdBuyer = ad.buyer === null ? '' : '<a href="list-valuations.html?user=' + ad.buyer + '" role="button">' + ad.buyer + '</a>';
                    var tr = '<tr><td>' + ad.offeredAmount + ' ' + ad.offeredCurrency + '</td>' +
                        '<td>' + ad.demandedAmount + ' ' + ad.demandedCurrency + '</td>' +
                        '<td>' + tdBuyer + '</td><td>' + ad.location + '</td>' +
                        '<td>' + ad.status + '</td><td>' + new Date(ad.date).toUTCString() + '</td>' +
                        '<td class="actions">' + tdActions + '</td></tr>';
                    $('#tableAds tbody').append(tr);
                });
                //Delete ad
                $('.btnDeleteAd')
                    .click(function (e) {
                        e.preventDefault();
                        deleteAd(getLocalToken(), $(this).attr('href'))
                            .done(function () {
                                location.reload();
                            })
                            .fail(function (jqXHR) {
                                showError(jqXHR.responseJSON);
                            })
                    });
            })
            .fail(function (jqXHR) {
                showError(jqXHR.responseJSON);
            });
        getPurchaseRequestsAsSeller(getLocalToken())
            .done(function (purchaseRequests) {
                $.each(purchaseRequests, function (key, request) {
                    var a = request.status == 'SOLD' ? '' : '<a class="btnSell" href="' + request.id + '" role="button"><i class="fas fa-check"></i></a>';
                    var tr = '<tr><td>' + request.offeredAmount + ' ' + request.offeredCurrency + '</td>' +
                        '<td>' + request.demandedAmount + ' ' + request.demandedCurrency + '</td>' +
                        '<td><a href="list-valuations.html?user=' + request.buyer + '" role="button">' + request.buyer + '</a></td>' +
                        '<td>' + request.location + '</td><td>' + new Date(request.date).toUTCString() + '</td>' +
                        '<td class="actions">' + a + '</td></tr>';
                    $('#tablePurchaseRequests tbody').append(tr);
                });
                //Sell Ad - Accept purchase request
                $('.btnSell')
                    .click(function (e) {
                        e.preventDefault();
                        sellAd(getLocalToken(), $(this).attr('href'))
                            .done(function () {
                                location.reload();
                            })
                            .fail(function (jqXHR) {
                                showError(jqXHR.responseJSON);
                            })
                    });
            })
            .fail(function (jqXHR) {
                showError(jqXHR.responseJSON);
            });
    }

    //Load purchase requests as buyer (if exist panel buyer)
    if ($('#panel-buyer').length) {
        getPurchaseRequestsAsApplicant(getLocalToken())
            .done(function (purchaseRequests) {
                $.each(purchaseRequests, function (key, request) {
                    var a = request.status == 'SOLD' ? '' : '<a class="btnDeletePurchaseRequest" href="' + request.id + '" role="button"><i class="fas fa-trash"></i></a>';
                    var tr = '<tr><td>' + request.offeredAmount + ' ' + request.offeredCurrency + '</td>' +
                        '<td>' + request.demandedAmount + ' ' + request.demandedCurrency + '</td>' +
                        '<td><a href="list-valuations.html?user=' + request.seller + '" role="button">' + request.seller + '</a></td>' +
                        '<td>' + request.location + '</td><td>' + request.status + '</td><td>' + new Date(request.date).toUTCString() + '</td>' +
                        '<td class="actions">' + a + '</td></tr>';
                    $('tbody').append(tr);
                });
                //Delete purchase request
                $('.btnDeletePurchaseRequest')
                    .click(function (e) {
                        e.preventDefault();
                        var button = $(this);
                        var tr = button.closest('tr');
                        deletePurchaseRequest(getLocalToken(), button.attr('href'))
                            .done(function () {
                                $(tr).remove();
                            })
                            .fail(function (jqXHR) {
                                showError(jqXHR.responseJSON);
                            })
                    });
            })
            .fail(function (jqXHR) {
                showError(jqXHR.responseJSON);
            });
    }

    //Load conversations (if exist list conversations)
    if ($('#list-conversations').length) {
        getConversations(getLocalToken())
            .done(function (conversations) {
                $.each(conversations, function (key, conversation) {
                    var user = getLocalUsername() != conversation.user1 ? conversation.user1 : conversation.user2;
                    var tr = '<tr><td><a href="list-valuations.html?user=' + user + '" role="button">' + user + '</a></td>' +
                        '<td class="col-num">' + conversation.numberMessages + '</td><td>' + new Date(conversation.date).toUTCString() + '</td>' +
                        '<td class="actions"><a href="list-messages.html?receiver=' + user + '" role="button"><i class="fas fa-comments"></i></a></td></tr>';
                    $('tbody').append(tr);
                });
            })
            .fail(function (jqXHR) {
                showError(jqXHR.responseJSON);
            });
    }

    //Load messages (if exist list messages)
    if ($('#list-messages').length) {
        getMessages(getLocalToken(), getUrlParameter('receiver'))
            .done(function (response) {
                $('#conversationId').val(response.conversationId);
                $.each(response.messages, function (key, message) {
                    var author = getLocalUsername() == message.author ? message.author : '<a href="list-valuations.html?user=' + message.author + '" role="button">' + message.author + '</a>';
                    var tr = '<div class="message container py-1 my-2 border rounded"><div class="row">' +
                        '<div class="col autor">' + author + '</div><div class="col date">' + new Date(message.date).toUTCString() + '</div></div>' +
                        '<div class="row"><div class="col messageContent py-3 text-justify">' + message.text + '</div></div></div>';
                    $('#messages').append(tr);
                });

                //Send message
                $('#formMessage')
                    .submit(function (e) {
                        e.preventDefault();
                        var message = {
                            conversationId: $('#conversationId').val(),
                            text: $('#messageContent').val()
                        }
                        createMessage(getLocalToken(), message)
                            .done(function () {
                                location.reload(true);
                            })
                            .fail(function (jqXHR) {
                                showError(jqXHR.responseJSON);
                            });
                    });
            })
            .fail(function (jqXHR) {
                showError(jqXHR.responseJSON);
                $('#formMessage').attr("disabled", true);
            });
    }

    //Create valuation
    $('#formValuation')
        .submit(function (e) {
            e.preventDefault();
            var text = $('#messageContent').val() == '' ? null : $('#messageContent').val();
            var valuation = {
                evaluated: $('#evaluated').val(),
                points: $('#points').val(),
                rol: $('#rol').val(),
                text: text
            }
            createValuation(getLocalToken(), valuation)
                .done(function () {
                    alert('Valuation created. Accept to go to home page');
                    $(location).attr('href', getUrlHome());
                })
                .fail(function (jqXHR) {
                    showError(jqXHR.responseJSON);
                });
        });

    //Load valuations (if exist list valuations)
    if ($('#list-valuations').length) {
        $('#evaluated').text(getUrlParameter('user'));
        getValuationsByEvaluated(getLocalToken(), getUrlParameter('user'))
            .done(function (valuations) {
                console.log(valuations);
                var sumS = 0, sumB = 0;
                $('#votesSeller').text(valuations.valuationsAsSeller.length);
                $.each(valuations.valuationsAsSeller, function (key, valuation) {
                    sumS += valuation.points;
                    $('#asSeller').append(getHTMLValuation(valuation));
                });
                $('#avgSeller').text(valuations.valuationsAsSeller.length == 0 ? 0 : sumS / valuations.valuationsAsSeller.length);

                $('#votesBuyer').text(valuations.valuationsAsBuyer.length);
                $.each(valuations.valuationsAsBuyer, function (key, valuation) {
                    sumB += valuation.points;
                    $('#asBuyer').append(getHTMLValuation(valuation));
                });
                $('#avgBuyer').text(valuations.valuationsAsBuyer.length == 0 ? 0 : sumB / valuations.valuationsAsBuyer.length);
            })
            .fail(function (jqXHR) {
                showError(jqXHR.responseJSON);
                $('#formMessage').attr("disabled", true);
            });
    }

});
