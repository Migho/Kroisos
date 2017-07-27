# Kroisos
_Make TKO-äly bookkeeping great again!_

Tämä on ohjelma jota tällä hetkellä käytetään TKO-älyn maksujärjestelmänä ja kirjanpidon apuohjelmana. Se päätettiin kehittää kun nykyinen rahastonhoitaja ei jaksanut ymmärtää miksi melkein 2000 tilitapahtumaa kirjataan käsin kirjanpitoon. Projekti alkoi lyhyestä tilioteparserista ja laajeni pian kaikki maksut hallitsevaksi järjestelmäksi, joka helpottaa maksujen kirjanpitoa huomattavasti ja vähentää käsintehtävää työtä.

Projekti on nykyään myös Helsingin yliopiston tietokantasovellus -kurssin työ. [Linkki dokumentaatioon](https://github.com/Migho/Kroisos/blob/master/doc/dokumentaatio.pdf)

## Kroisoksen tämänhetkiset toiminnot
Seuraavat toiminnallisuudet on enemmän tai vähemmän jo toteutettu ainakin bäkkärissä:
* Pitää kirjaa veloista SQL-tietokannassa
* Lisää SQL-tietokantaan uusia maksuja ja generoi niille viitenumerot
* Lähetä maksuohjeita tai maksumuistutuksia ihmisille
* Lue S-Pankin tiliote ja merkitse maksuissa käytettyjen viitenumeroiden perusteella maksuja maksetuksi.
