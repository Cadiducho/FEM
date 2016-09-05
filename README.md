# MeriCore #

Este es el README de la Repo del plugin MeriCore, MeriCore-Spigot y MeriCore-Bungee

### ¿Cómo puedo obtener el código? ###
Debido a la imposibilidad de colgar un servidor de Minecraft en un repositorio debido a las demandas de la DMCA, vía Maven solo se pueden conseguir las APIs, y MeriCore-Spigot hace uso de imports que corresponden al servidor en sí, por lo que antes de todo hay que ejecutar [BuildTools](https://www.spigotmc.org/threads/buildtools-updates-information.42865/).

* `curl "https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar" -o BuildTools.jar`
* `java -jar BuildTools.jar` Esto compilará Spigot en tu PC y lo añadirá al repositorio temporal en tu caché, por lo que ya tendrás la dependencia necesaria.
* ` git clone git@bitbucket.org:Meriland/mericore.git ` O usando SourceTree pulsando en 'Clone in SourceTree'
Una vez descargado el código y las dependencias, importa el proyecto de Maven en cualquier IDE de Java.

### Compilación ###
Suponiendo que ya tienes el código y las dependencias, compilar es lo más sencillo.

* Puedes ejecutarlo desde el propio IDE, o mediante el comando: `mvn clean install`
* Obtendrás los archivos compilados en la raiz del proyecto, con el nombre de 'MeriCore-Bungee' y 'MeriCore'