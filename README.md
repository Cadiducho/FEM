# FEM - UnderGames

FEM fue el nombre en clave que dí al proyecto antes de saber el nombre de UnderGames, servidor de minijuegos de Minecraft que finalmente no llegó a salir de fase de pruebas y se canceló en Febrero de 2017, habiendo sido desarrollado desde Junio de 2016 (el repositorio comienza en Agosto o Septiembre, si no me equivoco).

## Funcionamiento básico
  - Funciona con la API de Spigot, testeado con [PaperMC](https://github.com/PaperMC/Paper)
  - Está preparado y testeado para usarse con [Waterfall](https://github.com/WaterfallMC/Waterfall), aunque [BungeeCoord](https://github.com/SpigotMC/BungeeCord) sirve
  - Por desgracia, ciertas partes están en 1.8 y se podía entrar con un protocol-hack tipo [ViaVersion](https://github.com/MylesIsCool/ViaVersion)*
  - Con la ayuda de Bungee, cada plugin ejecuta UNA partida del juego. Necesitarás N servidores para N arenas
  - Usa third-party para versiones antiguas de Minecraft que fueron actualizadas por nosotros y que están compiladas en [mi repositorio Maven](https://cadiducho.com/repo), o bien están en en el código interior (como es el caso de utilidades de [InventiveTalent](https://github.com/InventivetalentDev))

'*' Aborrezco la idea de tener servidores en múltiples versiones y mantenerme en 1.8, versión de 2014, pero me obligaban. Por favor, no lo hagáis.

## Características
  - Sistema de rangos integrado. Es básico, si un comando es de rango 3 y tú eres 3 o más te dejará.
  - Un plugin que se tiene que ejecutar en todos los servidores para el manejo de su API interna y control de usuarios (FEM-Spigot)
  - Plugin de BungeeCord que actúa como 'satélite', rebotando datos de los plugins de Spigot.
  - Sistema de chat cross-server para los Lobby. El AdminChat va integrado en el lobby para que sea visible en TODOS los servidores. Los /tell son también cross-server. Estos pueden ser desactivados en tus ajustes del lobby.
  - Amistades estilo twitter. X eliges a quién seguir y a Y se le notifica que le sigues (a no ser que Y desactive esta notificación), pero no necesariamente Y va a seguir a X.
  - Ocultar jugadores y demás en base a amigos en el lobby... lo típico.
  - Sistema de carteles en el lobby para entrar, basado en [TeleportSigns](https://github.com/zh32/TeleportSigns) y modificado para nuestro uso.
  - *Nueve minijuegos*
    - GemHunter: Busca y destruye gemas por equipos
    - DyeOrDie: Corre por la plataforma buscando el color indicado, o muere cayendo al vacío
    - TntWars: Coloca la TNT en la isla del rival y asegúrate de que explote. Si un jugador muere habiendo sido destruida su isla es eliminado. Este juego tiene tienda con aldeanos y generadores de items por nivel. La TNT tiene cuenta atrás para explotar, y puede ser desactivada
    - TeamTntWars: Lo mismo, pero por equipos
    - Pictograma: Dibuja en la pizarra y que los otros jugadores lo adivinen. Como Pinturillo. La lista de palabras es configurable
    - BattleRoyale: Ármate y elimina al resto de los jugadores de la arena. Durante la partida caerán cofres con items más chetos que el resto de cofres (estos son marcados con un rayo de partículas temporalmente), y el WorldBorder irá encogiendo. Si pasa un tiempo sin acabar la partida, se iniciará una fase DeathMatch.
    - LuckyWarriors: Rompe los objetos sorpresa y craftea tu mejor equipo con lo obtenido. Los jugadores se enfrentarán en un coliseo a muerte, con tiempo y deathmatch también (aunque no se suele dar, este es sin duda el juego más rápido y frenético). Los items no se droppean totalmente aleatorios en los LuckyBlocks, si no que van por 'packs' de diferente nivel de habilidad. Cada Lucky roto es un nivel más de experiencia para encantar tus crafteos acto seguido.
    - SkyWars: No requiere mucha explicación. Realmente es como el TntWars pero simplificado totalmente.
    - Dropper: Minijuego muy casual para los lobbies. Cae esquivando los obstáculos hasta el final y obtiene todas los coleccionables. Bastante adictivo si los mapas están bien logrados. También hay insignias ocultas por el mapa.
  - *NO* tiene sistema de login y contraseñas. *NO* está preparado para ser un servidor pirata.
  - *NO* tiene sistema de baneos. Adquirí [LiteBans](https://www.spigotmc.org/resources/litebans.3715/) por mi cuenta hace tiempo y lo recomiendo.
  - *NO* tiene el protocol-hack integrado. Por favor, actualizad a la última versión de Minecraft... o en su defecto usad [ViaVersion](https://github.com/MylesIsCool/ViaVersion)
  - *NO* tiene los mapas de los juegos. Ni tengo acceso a ellos, ni fueron nuestro trabajo/creación. Derechos reservados a nuestros MapMakers
# ¿Qué necesito para utilizar el código?

  - Java 8. Es fundamental esto, no compilará con versiones anteriores
  - Maven 3. Es lo que usamos para el manejo de dependencias y la modularización del proyecto
  - Una base de datos MySQL. Es donde se almacenan los datos de los usuarios. Recomiendo [HeidiSQL](https://www.heidisql.com/) para la visualización/modificación de datos
  - Algo de paciencia, probablemente

## Compilación

Instala dependencias, inicia módulos y compila.

```sh
$ mvn clean install
```

Y carga el esquema de la base de datos en tu servidor.

Para su ejecución
* Configura la base de datos en la configuración de todos los FEMCore
* Instala tantos servidores como minijuegos quieras
* Arranca el servidor...

## Tips y comentarios / bugs
* Pictograma: Detecta que pintas con isBlocking(). Valen varias espadas en 1.8, o escudos en 1.9 y superiores. Si lo vas a usar con escudos, con durabilidad en los escudos (haciéndolos irrompibles y luego ajustándolo) se pueden diferenciar tipos de brochas. [Ver Java](https://github.com/Cadiducho/FEM/blob/82eb585797f24a80a644528a9f665923949ba7de/FEM-Pictograma/src/com/cadiducho/fem/pic/listener/GameListener.java#L45) y [ResourcePack](https://www.spigotmc.org/wiki/custom-item-models-in-1-9-and-up/)
* BattleRoyale. Algunos cofres quizás caen fuera del WorldBorder. A tener en cuenta. Las partículas de los cofres caídos dan MUCHO lag. Están configuradas a poco tiempo. No conseguí hacerlo con los rayos del beacon, parece ser algo client-side
* Los BossBar que muestra el nombre del servidor en 1.8 son horrendos e inconfigurables. Con 1.10 y 1.11 se podían hacer cosas muy guays con la nueva API de Spigot... probadlo.
* DyeOrDie. A veces se quitaban colores que SÍ eran los que salían o detectaba mal las regiones si no eran rectangulares... ¯\_(ツ)_/¯
* Pictograma. Con el cubo de pintura nos estuvo un tiempo persiguiendo un StackOverFlow... la función del cubo actúa de forma recursiva y a veces se volvía loca. Creo que al final estaba solucionado el error, pero quizás es buena idea limitarlo con un while.
* TeamTntWars: Se hizo bastante deprisa y a veces no interpretaba bien cuando un equipo entero había sido eliminado etc... el individual sí funciona bien
* Sobre los carteles: Modificamos algo de los motd y el envío de datos para nuestro uso, pero en general valen las guías mostradas por el plugin original

## ToDo
 - Mejorar el código internamente... la idea era hacerlo todo objetos, incluso las fases de los juegos
 - Terminar el Protections y el servidor survival en sí mismo
 - Unificar la estructura del código... tantos meses cambia mucho el código de un plugin antiguo a uno más moderno
 - Soporte para multiples idiomas, brevemente programado.
 - Hacer todos los ´team´ como una opción dentro del mismo plugin del individual
 - Demasiados cambios internos que ya se me han olvidado

## Licencias de terceros

FEM fue desarrollado con la ayuda y uso de:
* [BungeeCoord](https://github.com/SpigotMC/BungeeCord) - [BSD 3-clause](https://github.com/SpigotMC/BungeeCord/blob/master/LICENSE) - The Spigot Team
* [Bukkit/Spigot](https://hub.spigotmc.org/stash/projects/SPIGOT) - [GPL](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/browse/LICENCE.txt) - The Bukkit and Spigot Team
* [TeleportSigns](https://github.com/zh32/TeleportSigns) - [MIT](https://github.com/zh32/TeleportSigns/blob/development/LICENSE) - [zh32](https://github.com/zh32/)
* [ParticleAPI](https://github.com/InventivetalentDev/ParticleAPI) - ¿MIT? - [InventiveTalent](https://github.com/InventivetalentDev)
* [BossBarAPI](https://github.com/InventivetalentDev/BossBarAPI) - ¿MIT? - [InventiveTalent](https://github.com/InventivetalentDev)
* [ReflectionHelper](https://github.com/InventivetalentDev/ReflectionHelper) - [MIT](https://github.com/InventivetalentDev/ReflectionHelper/blob/master/LICENSE) - [InventiveTalent](https://github.com/InventivetalentDev)
* [MerchantsAPI](https://github.com/Cybermaxke/MerchantsAPI) - [LGPL-3.0](https://github.com/Cybermaxke/MerchantsAPI/blob/master/LICENSE.txt) - [Cybermaxke](https://github.com/Cybermaxke)
* [Lombok](https://projectlombok.org/) - [MIT](https://opensource.org/licenses/mit-license.php) - The Project Lombok Authors

*FEM* es liberado bajo una licencia MIT https://github.com/Cadiducho/FEM/blob/develop/LICENSE

## Comentarios de los desarrolladores

> Fueron unos buenos meses desarrollando este proyecto que finalmente no pudo salir adelante. Esperamos que a alguien le sirvan nuestros códigos e ideas. Estaríamos encantados de que nos contacteis con los usos que le déis, ya sea en algún plugin vuestro, un servidor o que hacéis un fork y continuáis el proyecto.
> Algunas partes del código pueden ser muy toscas y mejorables. Pido perdón por eso, ya que al empezar, con el poco tiempo que teníamos y todos los cambios que nos tocó hacer, no nos paramos a pensar mucho en la forma de hacerlo eficiente, solo en que funcionara.

# Contacto
* [Cadiducho](https://github.com/Cadiducho) - Desarrollador principal - cadiducho@gmail.com - [@Cadiducho](https://twitter.com/Cadiducho)
* [Cadox](https://github.com/cadox8) - Desarrollador durante los últimos meses - cadox8@gmail.com - [Web](http://cadox8.me) - [@cadox8](https://twitter.com/cadox8)

