// Módulos
let express = require('express');
let app = express();

let jwt = require('jsonwebtoken');
app.set('jwt', jwt);

let log4js = require('log4js');
log4js.configure({
    appenders: {wallapop: {type: 'file', filename: 'logs/wallapop.log'}},
    categories: {default: {appenders: ['wallapop'], level: 'trace'}}
});
let logger = log4js.getLogger('wallapop');
app.set('logger', logger);


app.use(function (req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Credentials", "true");
    res.header("Access-Control-Allow-Methods", "POST, GET, DELETE, UPDATE, PUT");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, token");
// Debemos especificar todas las headers que se aceptan. Content-Type , token
    next();
});


// Objeto sessión
let expressSession = require('express-session');
app.use(expressSession({
    secret: 'abcdefg',
    resave: true,
    saveUninitialized: true
}));

// Encriptación de contraseñas
let crypto = require('crypto');

// Base de datos
let mongo = require('mongodb');
let gestorBD = require("./modules/gestorBD.js");
gestorBD.init(app, mongo);
let swig = require('swig-templates');

let bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

app.use(express.static('public'));

// Variables
app.set('port', 8081);
app.set('db', 'mongodb://uo258525:sdi202206@sdi-actividad2-202-206-shard-00-00-01at8.mongodb.net:27017,sdi-actividad2-202-206-shard-00-01-01at8.mongodb.net:27017,sdi-actividad2-202-206-shard-00-02-01at8.mongodb.net:27017/test?ssl=true&replicaSet=sdi-actividad2-202-206-shard-0&authSource=admin&retryWrites=true');
app.set('clave', 'abcdefg');
app.set('crypto', crypto);


app.get('/', function (req, res) {
    res.redirect("/identificarse");
});

// routerUsuarioToken
let routerUsuarioToken = express.Router();
routerUsuarioToken.use(function (req, res, next) {
    // obtener el token, vía headers (opcionalmente GET y/o POST).
    let token = req.headers['token'] || req.body.token || req.query.token;
    if (token != null) {
        // verificar el token
        jwt.verify(token, 'secreto', function (err, infoToken) {
            if (err || (Date.now() / 1000 - infoToken.tiempo) > 24000) {
                res.status(403); // Forbidden
                res.json({
                    acceso: false,
                    error: 'Token invalido o caducado'
                });
                // También podríamos comprobar que intoToken.usuario existe
            } else {
                // dejamos correr la petición
                res.usuario = infoToken.usuario;
                next();
            }
        });
    } else {
        res.status(403); // Forbidden
        res.json({
            acceso: false,
            mensaje: 'No hay Token'
        });
    }
});
// Aplicar routerUsuarioToken
app.use('/api/oferta', routerUsuarioToken);
app.use('/api/offer/message/:id', routerUsuarioToken);
app.use('/api/offer/conversation/:id', routerUsuarioToken);
app.use('/api/mensaje/eliminar/', routerUsuarioToken);
app.use('/api/mensaje/leido/:id', routerUsuarioToken);
app.use('/api/offer/listConversations', routerUsuarioToken);
app.use('/api/search/offer/conversation/:id', routerUsuarioToken);
app.use('/api/conversation/delete/:id', routerUsuarioToken);

// routerUsuarioSession
let routerUsuarioSession = express.Router();
routerUsuarioSession.use(function (req, res, next) {

    if (req.session.user) {
        // dejamos correr la petición
        next();
    } else {
        res.redirect("/identificarse");
    }
});

app.use("/home", routerUsuarioSession);
app.use("/offer/*", routerUsuarioSession);
app.use("/admin", routerUsuarioSession);
app.use("/user/*", routerUsuarioSession);

//asegurar identificacion
let routerAdmin = express.Router();
routerAdmin.use(function (req, res, next) {
    if (req.session.user !== undefined && req.session.user.rol === 'admin') {
        // dejamos correr la petición
        next();
    } else {
        res.redirect("/home");
    }
});
app.use("/admin", routerAdmin);
app.use("/resetdb", routerAdmin);
app.use("/addAdmin", routerAdmin);
app.use("/user/*", routerAdmin);
//Rutas/controladores por lógica

//Maneja rutas respecto a usuarios
require("./routes/rusuarios.js")(app, swig, gestorBD);
//Maneja rutas respecto a ofertas
require("./routes/rofertas.js")(app, swig, gestorBD);
//Manaeja rutas respecto a admin
require("./routes/radmin.js")(app, swig, gestorBD);
//Manaeja rutas de api
require("./routes/rapiofertas.js")(app, gestorBD);


// lanzar el servid
app.listen(app.get('port'), 8081, function () {
    console.log("Servidor activo en puerto " + app.get('port'));
});
//mongo
