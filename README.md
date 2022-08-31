# TOTECO APP

_Se trata de una app a modo de red social para puntuar los desayunos, almuerzos o meriendas. Se puntuará el establecimiento, cada uno de los productos que se han tomado y el precio de los mismos, se adjuntará una fotografía de la publicación y al añadirla se mostrará en la pantalla principal como si fuese una publicación de una red social._

## Explicación de pantallas 📱

### MAIN ACTIVITY

![Screenshot](MainActivity.png)

_La pantalla principal se trata del login, en el que un usuario registrado puede iniciar sesión poniendo su usuario y contraseña. Si el usuario o contraseña no son correctos o no está registrado al darle al botón de acceder aparecerá un mensaje de error. Debajo del botón, si se clicka en la frase se accede a la pantalla de registrar un usuario._

### REGISTER ACTIVITY

![Screenshot](RegisterActivity.png)

_La pantalla de registro sirve para que el usuario se registre y pueda iniciar sesión. Para ello tiene que introducir los campos usuario, nombre, apellido, fecha de nacimiento y contraseña. Todos los campos deben estar rellenos, en caso de que no, al darle al botón de registrarse aparecerá un mensaje de error. En caso de que las contraseñas introducidas no coincidan también aparecerá un mensaje de error. Si todo va bien, al registrarse se pasará a la pantalla de inicio de sesión y aparecerá un pop up diciendo que el usuario ha sido registrado con éxito._

### PUBLICATIONS ACTIVITY

![Screenshot](PublicationsActivity.png)

_En la pantalla de publicaciones se mostrarán todas las publicaciones que ha realizado un usuario ordenadas por fecha de más nueva a más antigua. La publicación consta de un título con el nombre del establecimiento, la imagen y a la derecha los productos que se han consumido, el precio y la puntuación de los mismos, y por último tanto el precio total como la puntuación total. Si es un usuario nuevo la pantalla aparecerá vacía hasta que se haga la primera publicación._

![Screenshot](DeletePublicationDialog.png)

_Si se hace una pulsación larga en una de las publicaciones aparece el diálogo de confirmación de borrado, si se pulsa en confirmar la publicación se eliminará de la base de datos y se refrescará la lista quitando esa publicación de la pantalla._

_Además, la pantalla cuenta con una toolbar con diferentes iconos. El primero es el icono de localizaciones, al pulsar se llevará a la correspondiente pantalla, el siguiente es el de añadir una nueva publicación y por último tenemos el de cerrar sesión._

### LOCATIONS ACTIVITY

![Screenshot](LocationsActivity.png)

_La pantalla de localizaciones es un mapa en el que aparecen marcados todos los establecimientos registrados por el usuario. Al pulsar encima de cualquier marcador aparece un diálogo con el nombre del establecimiento y la puntuación del mismo._

### ADD PUBLICATION ACTIVITY

![Screenshot](AddPublicationActivity.png)

_Lo primero que aparece en esta pantalla es la elección de establecimiento, si se pulsa redirige a la pantalla de selección de establecimiento:_

![Screenshot](AddEstablishmentActivity.png)

_En esta pantalla se puede introducir el nombre del establecimiento y la puntuación del mismo. Debajo aparece un mapa para seleccionar uno de los estableimientos ya registrados o añadir uno nuevo. En caso de seleccionar uno ya registrado el campo de nombre se rellena automáticamente con el nombre del establecimiento seleccionado y se deshabilita su edición. En caso de pulsar un lugar del mapa en el que no haya marcadores se limpia el nombre y se habilita la edición para que el usuario pueda introducir el nombre del nuevo establecimiento. Al darle al botón de añadir si alguno de los campos no está relleno aparecerá un mensaje de error, sin embargo, si todo va bien se registrará el establecimiento en caso de que sea nuevo y se redirigirá a la pantalla de añadir publicación._

_De vuelta a la pantalla se pintará el nombre del establecimiento seleccionado y la puntuación introducida en los cuadros de texto que previamente estaban vacíos. A continuación se puede añadir un producto o seleccionar una imagen del archivo del móvil._

![Screenshot](AddProductDialog.png)

_Al pulsar en añadir producto aparece un cuadro de diálogo con los campos nombre, precio y puntuación. Si se le da a añadir con alguno o todos los campos vacíos saltará un pop up indicando el error. Si se rellenan los campos se registrará el producto y aparecerá en la lista de productos. Cada vez que se registre, modifique o elimine un producto se refresca la lista. También se actualizan los campos puntuación y precio._

![Screenshot](ModifyProductFragment.png)

_Una vez introducido un producto si se hace una pulsación corta sobre él aparecerá el cuadro de diálogo de modificar producto con los campos nombre, precio y puntuación rellenos en base al producto seleccionado. Al modificar alguno de los campos se actualizará el producto._

![Screenshot](DeleteProductDialog.png)

_Si se hace una pulsación larga sobre alguno de los productos aparecerá el diálogo de eliminar producto. Si se le dá a eliminar se eliminará de la base de datos y de la lista de productos._

_Con el establecimiento seleccionado, los productos añadidos y la imagen seleccionada se puede añadir la publicación, en caso de que falte alguno de esos campos aparecerá un mensaje de error. Al añadir la publicación se redirige a la pantalla de publicaciones y se mostrará en primer lugar la nueva publicación, además aparecera un mensaje de que se ha realizado la publicación con éxito._

## Comenzando 🚀

_Estas instrucciones te permitirán obtener una copia del proyecto en funcionamiento en tu máquina local para propósitos de desarrollo y pruebas._


### Pre-requisitos 📋

_Cosas que necesitas para instalar el software y como instalarlas_

```
- Tener instalado java en el ordenador
- Tener instalado Android Studio
- Tener un emulador de android compatible con la versión del proyecto y con acceso a la Play Store (sin ese acceso no funcionarán los mapas)
```

### Instalación 🔧

_Una serie de ejemplos paso a paso que te dice lo que debes ejecutar para tener un entorno de desarrollo ejecutandose_

```
- Clonar el este repositorio y situarse en la rama AA1
- Lanzar la aplicación en un emulador o móvil Android físico
```

## Construido con 🛠️

* [Android Studio](https://developer.android.com) - Framework usado
* [Gradle](https://gradle.org) - Gestor de dependencias

## Autores ✒️

* **Irene Cunto** - *Trabajo Principal y documentación* - [cunba](https://github.com/cunba)

También puedes mirar la lista de todos los [contribuyentes](https://github.com/cunba/toteco_app) que han participado en este proyecto.


---
⌨️ con ❤️ por [cunba](https://github.com/cunba) 😊
