module.exports = function (app, gestorBD) {
    app.post("/api/autenticar/", function (req, res) {
        var seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
        var criterio = {
            email: req.body.email,
            password: seguro
        }

        gestorBD.obtenerUsuarios(criterio, function (usuarios) {
            if (usuarios == null || usuarios.length === 0) {
                res.status(401); // Unauthorized
                res.json({
                    autenticado: false
                })
            } else {
                var token = app.get('jwt').sign(
                    {usuario: criterio.email, tiempo: Date.now() / 1000},
                    "secreto");
                res.status(200);
                res.json({
                    autenticado: true,
                    token: token
                })
            }

        });
    });
    app.get("/api/oferta", function (req, res) {
        var token = req.headers['token'] || req.body.token || req.query.token;
        app.get('jwt').verify(token, 'secreto', function (err, infoToken) {
            if (err) {
                app.get("logger").error('API: Token invalido');
                res.status(403); // Forbidden
                res.json({
                    acceso: false,
                    error: 'Token invalido o caducado'
                });
                // También podríamos comprobar que intoToken.usuario existe
            } else {
                // dejamos correr la petición
                var usuario = infoToken.usuario;
                cri = {
                    owner: {$ne: res.usuario},
                    state: {$ne: 'no disponible'}
                }
                gestorBD.obtenerOfertas(cri, function (ofertas) {
                    if (ofertas == null || ofertas.length === 0) {
                        app.get("logger").erro('Usuario no autorizado');
                        res.status(204); // Unauthorized
                        res.json({
                            err: "No results"
                        });
                    } else {
                        app.get("logger").info('API: Se han mostrado las ofertas disponibles para un usuario');
                        res.status(200);
                        res.json({offers: ofertas});
                    }
                })
            }
        })
    });

    app.post('/api/offer/message/:id', function (req, res) {
        let criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};

        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas == null || ofertas.length === 0) {
                res.status(403); // Unauthorized
                res.json({
                    err: "No results"
                });
            } else {
                let usuario = res.usuario;
                let oferta = ofertas[0];
                let message = {
                    sender: usuario,
                    offer: oferta,
                    message: req.body.message,
                    date: new Date(),
                    read: false

                };
                gestorBD.insertarMensaje(message, function (mensaje) {
                    if (mensaje == null) {
                        res.status(500); // error del servidor
                        res.json({
                            err: "Error del servidor"
                        });
                    } else {
                        res.status(200);
                        res.json(JSON.stringify(mensaje));
                    }
                })
            }
        });
    });
};
