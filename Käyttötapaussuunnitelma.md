# KÄYTTÖTAPAUSSUUNNITELMA

## Käyttäjät
Ohjelmassa on seuraavat käyttäjät:
  * "Normaali käyttäjä"
  * Laskutuskäyttäjä
  * Toiminnantarkastaja
  * Admin
  
*Normaali käyttäjä* voi luoda kulukorvauksia omissa nimissään, muokata niiden tietoja (ennen kuin ne on hyväksytty) sekä tarkastella omia maksujaan (sekä maksamattomia että maksettujen historiaa). Lisäksi käyttäjä voi luoda budjetteja, joita kokouksessa hyväksymisen jälkeen rahastonhoitaja hyödyntää toteumissa. Jos jäsenmaksua ei ole maksettu, järjestelmä antaa käyttäjälle maksuohjeet jäsenmaksua varten muttei anna tehdä kulukorvauksia.

*Laskutuskäyttäjillä* on oikeus luoda lisäksi laskuja. Tämä on lähinnä yritysvastaaville tarkoitettu ominaisuus.

*Toiminnantarkastaja* on käyttäjä, jolla on oikeus tarkastella kaikkea vuoden tietoa sen jälkeen kun rahastonhoitaja on koonnut tilinpäätöksen. Toiminnantarkastaja ei voi suoranaisesti voi muokata kirjanpitoa, mutta voi kirjoittaa kommentteja ja huomautuksia tositteisiin ja lopulta katsoa kirjanpidon hyväksytyksi.

*Admin* on järjestelmää ylläpitävä taho, ja potentiaalisia admineja ovat rahastonhoitaja ja puheenjohtaja. Adminilla on oikeus muokata kaikkien kulukorvauksia, hyväksyä niitä sekä yhdistellä kirjanpidossa näkyviin tositteisiin. Admin voi lisätä maksuja jäsenille ja ihmisryhmille, muokata niitä, lähettää maksuohjeita ja maksumuistutuksia jäsenille sekä yhdistellä saapuneita maksuohjeita kirjanpidossa näkyviin tositteisiin. Budjeteista on voitava koota esityskelpoisia toteumia. Lisäksi käytössä on tilinpäätöksen tekemiseen liittyvät koontityökalut ja muut toiminnot.

## Tapahtumat
Tapahtumia varten on tapahtumaID, joka on yleisimmin TKO-äly members-sivuilla tapahtumalinkeissä näkyvä nelinumeroinen luku. Tapahtuma voi olla myös esimerkiksi jäsenmaksu. Oleellista on se, että tapahtumaID:n avulla saadaan selville mistä tilistä kyseiselle tapahtumalle osoitetut transaktiot menevät.

## Kulukorvaukset eli järjestön omat velat
Kulukorvaukseen saadaan käyttäjän tietojen kautta saaja. Käyttäjä valitsee tilin minne maksetaan ja liittää tarvittavat (mahdollisesti skannatut) dokumentit ja kuitit kulukorvaukseen. Halutessaan käyttäjä voi myös asettaa mistä budjetista kulukorvauksen on tarkoitus mennä sekä mahdolliset lisätiedot.

Kulukorvauksia on kahdenlaisia:
  * Bensakulukorvaus, jossa anoja lisää ylläolevien lisäksi myös ajetun matkan. Maksettava summa määräytyy hallituksen määrittelemän kilometrikorvauksen mukaan.
  * "Normaali" kulukorvaus, jossa anoja lisää ylläolevien lisäksi myös lopullisen pyydettävän summan.
  
Rahastonhoitaja tapauskohtaisesti voi luoda vielä hyväksymättömistä luoduista kulukorvauksista dokumentin, jota käytetään esimerkiksi budjetoimattomien kulukorvausten hyväksymiseen.

## Maksut eli muiden velat järjestölle
Kroisoksen ensisijainen idea oli tehdä maksujen ylläpidosta järjestelmällistä ja automaattista. Järjestelmän on tarkoitus repiä automaattisesti members-sivulta tietyn tapahtuman osallistujat ja heidän ilmoittamat osallistumistiedot ja luoda niistä maksut käyttäjille. Rahastonhoitaja voi muokata haluamansa mukaan maksuja ja lähettää valitsemistaan maksuista maksuohjeita tai maksumuistutuksia. Järjestelmä lisää hallituksen määrittelemän maksumuistutusmäärän jälkeen kiinteän muistutusmaksun. Maksut itsessään sisältävät summan, tapahtumaID (kuten jäsenmaksu, vuosijuhlamaksu 2016, fuksisitsimaksu 2016 jne) sekä viitenumeron, josta viimeinen numero on tarkistusnumero, kolme seuraavaavaa on järjestyksessä kasvava osallistujanumero ja loput numerot ovat tapahtumaID-numeroa.

Normaalit käyttäjät voivat puolestaan tarkastella omia maksuhistorioitaan ja vielä maksamattomia maksuja.

## Budjetit ja toteumat
Kukin jäsen voi luoda budjetteja, jotka rahastonhoitaja merkkaa kokousten päätösten mukaan hyväksytyiksi tai hylätyiksi. Budjeteissa on voitava määritellä erikseen menoja ja tuloja sekä kunkin tulon ja menon määrä. Lisäksi määritellään tapahtumaID. Järjestelmä automaattisesti laskee tulojen ja menojen summan ja laskee lopullisen budjetin. Järjestelmä automaattisesti kasaa toteumaa sitä mukaa kun laskuja ja kulukorvauksia liitetään kyseiseen budjettiin. Nykyisen toteuman lisäksi järjestelmä laskee myös lopullisen toteuman, johon lasketaan myös vielä saatavat maksut osallistujilta.

## Kirjanpito ja tilinpäätös
Järjestelmään syötetään manuaalisesti S-Pankista tiliotteita. Järjestelmä lisää tiliotteissa näkyvät tilitapahtumat eli transaktiot kirjanpitoon ja viitenumeron perusteella päättelee mille tilille maksun on tarkoitus mennä. Jos tiliä ei voi päätellä, se ei kuulu mihinkään tiliin ja rahastonhoitaja korjaa maksutapahtuman myöhemmin manuaalisesti tilinpäätöstä varten. Transaktiot voivat sisältää selitteen ja/tai viitata johonkin kulukorvaukseen tai käyttäjän maksamaan maksuun, josta taas saadaan tapahtumaID. Vaihtoehtoisesti transaktio voi sisältää dokumentin, jolloin esimerkiksi lasku voidaan liittää transaktioon tositteeksi.

Lopullista tilinpäätöstä varten on luotava erilaisia dokumentteja, kuten esimerkiksi tase. Lisäksi dokumentit ovat tarpeellisia esimerkiksi budjetoinnissa.
