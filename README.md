# Ponesi

Ponesi je Android aplikacija za pravljenje i organizovanje lista za pakovanje. Aplikacija je razvijena kao praktični deo diplomskog rada.

## Funkcionalnosti

* Kreiranje, izmena i brisanje šablona za putovanja
* Organizovanje stvari po kategorijama
* Zakazivanje, pokretanje i završavanje putovanja
* Opciona destinacija i vremenska prognoza za zakazana i aktivna putovanja
* Prognoza za približnu trenutnu lokaciju korisnika
* Istorija završenih putovanja sa datumima, lokacijom i fotografijom
* Označavanje spakovanih stvari
* Filtriranje i sortiranje putovanja
* Čuvanje i otvaranje PDF i slikovnih dokumenata
* Tri početna šablona dostupna pri prvom pokretanju

## Tehnologije

* Kotlin
* Jetpack Compose
* Room
* MVVM arhitektura
* Material 3
* Open-Meteo API
* Google Play services Location

Aplikacija zadržava osnovne funkcionalnosti i bez internet veze. Vremenski podaci su obezbeđeni putem [Open-Meteo](https://open-meteo.com/).

## Pokretanje projekta

Projekat je potrebno otvoriti u Android Studiju, sačekati završetak Gradle sinhronizacije i pokrenuti `app` konfiguraciju na emulatoru ili Android uređaju.

Minimalna podržana verzija sistema je Android 8.0 (API 26).

## Autor

Lazar Nedeljković