// TODO Extraer a otro documento INICIO

function getToday() {
    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth() + 1; //January is 0!
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
    if ($('#login-user').length || $('#update-user').length || $('#historical-ratios').length || $('#calculate-price').length) {
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
                });;

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
                    });;
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

    //Load currency list (if exist currency id)
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

});
