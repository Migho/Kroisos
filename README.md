# Kroisos
_Make TKO-äly bookkeeping great again!_

Tämä on ohjelma jota tällä hetkellä käytetään TKO-älyn maksujärjestelmänä ja kirjanpidon apuohjelmana. Se päätettiin kehittää kun nykyinen rahastonhoitaja ei jaksanut ymmärtää miksi melkein 2000 tilitapahtumaa kirjataan käsin kirjanpitoon. Projekti alkoi lyhyestä tilioteparserista ja laajeni pian kaikki maksut hallitsevaksi järjestelmäksi, joka helpottaa maksujen kirjanpitoa huomattavasti ja vähentää käsintehtävää työtä.

## Kroisoksen tämänhetkiset toiminnot
HUOM! Kroisos on kokenut hiljattain melko laajan muodonmuutoksen. Backendiä on muutettu, ja alkuperäistä tekstipohjaista käyttöliittymää ei enää tueta ja se on poistettu. Vaadin-kirjastolla toteutettava frontend taas on kesken.
Seuraavat toiminnallisuudet on enemmän tai vähemmän jo toteutettu ainakin bäkkärissä:
* Pitää kirjaa veloista SQL-tietokannassa
* Lisää SQL-tietokantaan uusia maksuja ja generoi niille viitenumerot
* Lähetä maksuohjeita tai maksumuistutuksia ihmisille
* Lue S-Pankin tiliote ja merkitse maksuissa käytettyjen viitenumeroiden perusteella maksuja maksetuksi
* ~~Teetä tiliotteesta TKO-älyn kirjanpitoon suoraan sopiva excel-taulukko.~~ Ei enää tueta, tulevaisuudessa kirjanpito on muussa muodossa.

## Tulevaisuus
Tarkoitus olisi ainakin lähiaikoina vääntää pankkiviivakoodi sähköposteihin, ja kehitteillä on myös webbikäli. Jos innostuit projektista tai haluat muuten vain rupatella mukavia niin rupatellaan ihmeessä, terveisin Tekiksen vuoden 2016 rahastonhoitaja.
