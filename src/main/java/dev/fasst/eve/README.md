# FasstEve (Fabric, Minecraft 1.21.4)

Prawy Alt otwiera GUI. Wybierasz preset, a przedmiot w ręce dostaje inną nazwę i model
TYLKO na Twoim kliencie (podgląd lokalny). Serwer nic o tym nie wie.

## Budowanie
Wymagania: JDK 21 i Gradle 8.10+ (albo wrapper wygenerowany komendą `gradle wrapper`).

    gradle build

Gotowy plik: `build/libs/FasstEve-1.0.0.jar` (zmień nazwę na FasstEve.jar).
Wrzuć do `.minecraft/mods` razem z Fabric Loader i Fabric API dla 1.21.4.

## Własne presety
Edytuj listę PRESETS w `FasstEveScreen.java` (nazwa, id modelu, kolor).
