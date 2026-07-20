# SMP Spectator Return

Simple server-side Fabric mod for **Minecraft 1.21.10**.

**Author:** JaraSG2  
**Mod version:** 1.0.1

![Minecraft](https://img.shields.io/badge/Minecraft-1.21.10-green)
![Fabric](https://img.shields.io/badge/Loader-Fabric-orange)
![License](https://img.shields.io/badge/License-MIT-blue)
![Server](https://img.shields.io/badge/Server-Side-success)

## English

### Features

- `/s` saves the player's current world, position, rotation and game mode.
- The player is switched to Spectator mode.
- Using `/s` again returns the player to the saved location.
- Survival, Creative or Adventure mode is restored automatically.
- Saved positions survive server restarts and crashes.
- The mod is required only on the server.
- `/s reload` reloads the configuration and requires permission level 4.

### Configuration

The configuration file is created after the first server start:

```text
config/smp-spectator-return.json
```

Default configuration:

```json
{
  "Enabled": true,
  "AllowOnCreative": false,
  "AllowOnAdventure": false,
  "AllowedWorlds": [
    "*"
  ]
}
```

| Option | Description |
|---|---|
| `Enabled` | Enables or disables entering Spectator mode with `/s`. |
| `AllowOnCreative` | Allows the command to be used from Creative mode. |
| `AllowOnAdventure` | Allows the command to be used from Adventure mode. |
| `AllowedWorlds` | Worlds where entering Spectator mode is allowed. Use `"*"` for all worlds. |

Example for Overworld and Nether only:

```json
{
  "Enabled": true,
  "AllowOnCreative": true,
  "AllowOnAdventure": false,
  "AllowedWorlds": [
    "minecraft:overworld",
    "minecraft:the_nether"
  ]
}
```

Custom dimensions must use their full identifier, for example:

```text
modid:dimension_name
```

A player with an existing saved return position can always use `/s` to return, even if the mod or the original world has since been disabled in the configuration.

### Build

Requirements:

- Java 21
- Internet connection for the first Gradle run

Windows:

```powershell
.\gradlew.bat clean build
```

Linux/macOS:

```bash
./gradlew clean build
```

The compiled mod is created in:

```text
build/libs/smp-spectator-return-1.0.0.jar
```

Do not upload the `-sources.jar` file to the server.

---

## Česky

### Funkce

- `/s` uloží aktuální svět, pozici, natočení a herní režim hráče.
- Hráč se přepne do Spectatora.
- Druhé `/s` hráče vrátí na uložené místo.
- Automaticky se obnoví Survival, Creative nebo Adventure.
- Uložené pozice zůstanou zachované po restartu i pádu serveru.
- Mod stačí nainstalovat pouze na server.
- `/s reload` znovu načte konfiguraci a vyžaduje oprávnění úrovně 4.

### Konfigurace

Po prvním spuštění serveru se vytvoří:

```text
config/smp-spectator-return.json
```

Výchozí konfigurace:

```json
{
  "Enabled": true,
  "AllowOnCreative": false,
  "AllowOnAdventure": false,
  "AllowedWorlds": [
    "*"
  ]
}
```

| Nastavení | Popis |
|---|---|
| `Enabled` | Zapne nebo vypne vstup do Spectatora přes `/s`. |
| `AllowOnCreative` | Povolí použití příkazu z Creative. |
| `AllowOnAdventure` | Povolí použití příkazu z Adventure. |
| `AllowedWorlds` | Seznam světů, kde je možné Spectatora zapnout. Hodnota `"*"` povolí všechny světy. |

Příklad pouze pro Overworld a Nether:

```json
{
  "Enabled": true,
  "AllowOnCreative": true,
  "AllowOnAdventure": false,
  "AllowedWorlds": [
    "minecraft:overworld",
    "minecraft:the_nether"
  ]
}
```

Pro vlastní dimenze použij úplný identifikátor, například:

```text
modid:nazev_dimenze
```

Hráč s uloženou návratovou pozicí se může druhým `/s` vždy vrátit, i když byl mod nebo původní svět mezitím v konfiguraci vypnutý.

### Sestavení

Požadavky:

- Java 21
- Připojení k internetu při prvním spuštění Gradlu

Windows:

```powershell
.\gradlew.bat clean build
```

Linux/macOS:

```bash
./gradlew clean build
```

Hotový mod vznikne zde:

```text
build/libs/smp-spectator-return-1.0.0.jar
```

Na server nenahrávej soubor s koncovkou `-sources.jar`.
