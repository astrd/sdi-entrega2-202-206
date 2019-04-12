// Módulos
var express = require('express');
var app = express();

app.use(function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Credentials", "true");
    res.header("Access-Control-Allow-Methods", "POST, GET, DELETE, UPDATE, PUT");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, token");
// Debemos especificar todas las headers que se aceptan. Content-Type , token
    next();
});

var jwt = require('jsonwebtoken');
app.set('jwt', jwt);

// Objeto sessión
var expressSession = require('express-session');
app.use(expressSession({
    secret: 'abcdefg',
    resave: true,
    saveUninitialized: true
}));

// Encriptación de contraseñas
var crypto = require('crypto');

// Base de datos
var mongo = require('mongodb');

var swig = require('swig-templates');

var bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.use(express.static('public'));

// Variables
app.set('port', 8081);
app.set('db','mongodb://uo258525:sdi202206@sdi-actividad2-202-206-shard-00-00-01at8.mongodb.net:27017,sdi-actividad2-202-206-shard-00-01-01at8.mongodb.net:27017,sdi-actividad2-202-206-shard-00-02-01at8.mongodb.net:27017/test?ssl=true&replicaSet=sdi-actividad2-202-206-shard-0&authSource=admin&retryWrites=true');
app.set('clave','abcdefg');
app.set('crypto',crypto);

// routerUsuarioSession
var routerUsuarioSession = express.Router();
routerUsuarioSession.use(function(req, res, next) {
    console.log("routerUsuarioSession");
    if ( req.session.usuario ) {
        // dejamos correr la petición
        next();
    } else {
        console.log("va a : "+req.session.destino)
        res.redirect("/identificarse");
    }
});


//Rutas/controladores por lógica
//Rutas/controladores por lógica
require("./routes/rusuarios.js")(app, swig);
require("./routes/rofertas.js")(app, swig);

app.get('/', function (req, res) {
    let respuesta = swig.renderFile('views/home.html', {});
    res.send(respuesta);
});
// lanzar el servid
app.listen(app.get('port'), 8081, function() {
    console.log("Servidor activo en puerto " + app.get('port'));
});
//mongo
