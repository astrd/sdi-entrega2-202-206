module.exports = function (app, swig, gestorBD) {
    app.get("/admin", function (req, res) {
        let respuesta = swig.renderFile('views/admin.html', {
            user: req.session.user
        });
        app.get("logger").info('Admin se ha dirijido a la pagina de administracion principal');
        res.send(respuesta);
    });

    app.get("/resetdb", function (req, res) {
        gestorBD.resetDB(function (result) {
            if (result == null) {
                res.redirect("/identificarse?mensaje=Error al resetear las colecciones&tipoMensaje=alert-danger");
            } else {
                app.get("logger").info('Admin ha reseteado la base de datos');
                addAdmin(res);
            }
        });
    });

    function addAdmin(res) {
        let seguro = app.get("crypto").createHmac('sha256',
            app.get('clave')).update('admin').digest('hex');
        let cri = {email: 'admin@email.com'};
        gestorBD.obtenerUsuarios(cri, function (usuarios) {
            if (usuarios != null && usuarios.length !== 0) {
                res.redirect("/identificarse?mensaje=Admin ya existe" + "&tipoMensaje=alert-danger ");
            } else {
                let admin = {
                    name: 'admin',
                    surname: 'admin',
                    email: 'admin@email.com',
                    password: seguro,
                    rol: 'admin',
                    money: 100.0,
                    valid: true
                };
                gestorBD.insertarUsuario(admin, function (id) {
                    if (id == null) {
                        app.get("logger").error('Error registro de admin');
                        res.redirect("/identificarse?mensaje=Error al insertar admin en reset&tipoMensaje=alert-danger");
                    } else {
                        app.get("logger").info('Usuario admin creado');
                        createData(res);
                    }
                })
            }
        })
    }

    function createData(res) {
        const listUsers = [];
        const limitUsers = 10;
        const numOffersUser = 5;
        const numMessageUser = 2;
        for (let i = 1; i <= limitUsers; i++) {
            let password = app.get("crypto").createHmac('sha256',
                app.get('clave')).update('user' + i).digest('hex');
            let user = {
                name: 'user' + i,
                surname: 'user' + i,
                email: 'user' + i + '@email.com',
                password: password,
                rol: 'user',
                money: 100.0,
                valid: true
            };
            listUsers.push(user);
        }
        gestorBD.insertDataTest(listUsers, 'usuarios', function (users) {
            if (users == null || users.length === 0) {
                res.redirect("/identificarse?mensaje=Error al crear los usuarios de prueba");
            } else {
                const listOffers = [];
                listUsers.forEach(user => {
                    for (let i = 1; i <= numOffersUser; i++) {
                        let offer = {
                            title: 'Oferta ' + i + ' del usuario ' + user.name,
                            description: 'Descripcion de la oferta ' + i + ' del usuario ' + user.name,
                            price: i * 5,
                            owner: user.email,
                            state: 'disponible',
                            buyer: 'none',
                            fav: ''
                        };
                        listOffers.push(offer);
                    }
                });
                gestorBD.insertDataTest(listOffers, 'ofertas', function (offers) {
                    if (offers == null || offers.length === 0) {
                        res.redirect("/identificarse?mensaje=Error al crear las ofertas de prueba");
                    } else {
                        const listMessages = [];
                        listOffers.forEach(offer => {
                            listUsers.forEach(user => {
                                if (user.email !== offer.owner) {
                                    for (let i = 1; i <= numMessageUser; i++) {
                                        let message = {
                                            sender: user.email,
                                            receiver: offer.owner,
                                            offer: offer._id,
                                            message: 'Mensaje ' + i + 'del usuario ' + user.name,
                                            date: new Date(),
                                            read: false
                                        };
                                        listMessages.push(message);
                                    }
                                }
                            });
                        });
                        gestorBD.insertDataTest(listMessages, 'mensajes', function (messages) {
                            if (messages == null || messages.length === 0) {
                                res.redirect("/identificarse?mensaje=Error al crear las ofertas de prueba");
                            } else {
                                res.redirect("/identificarse?mensaje=Datos creado correctamente");
                            }
                        });
                    }
                });
            }
        });
    }
}
;
