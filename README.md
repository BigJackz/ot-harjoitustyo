# HarjoitusTyö
## The Cursed Game
Pelissä voi liikkua käyttämällä näppäimiä A ja D ja hyppiä välilyönnillä.
Pelin hyppy jää ns. "varastoon" ja uuden hypyn saa aina kun laskeutuu vihreälle alustalle.
Pelissä vihreät palikat ovat tasoja joissa voi olla, mustat palikat taas ansoja joihin kuolee.
Pelistä löytyy 3 ensimmäistä karttaa.
Pitäkää hauskaa :D
HUOM! Huomasin liian myöhään että peli toimii erittäin hitaasti 60hz näytöillä, sillä menin tekemään kaikki testailut 144hz näytöllä.
Jos testaat tätä 60hz näytöllä niin älä huoli en tarkoittanut peliä niin hitaaksi mille se vaikuttaa, yritän korjata tämän pian, jos edes voin...
### Erikoislaatat: 
Pinkit pienet laatikot antavat myös hypyn takaisin pelaajalle.
Vaaleansiniset pienet laatikot taas kasvattavat pelaajan nopeutta tietyksi määräksi liikkumista, nämä kasaantuvat eli jos keräät niitä kaksi kasvaa nopeutesi kahdella joksikin aikaa.
Sininen keskikokoinen laatikko on checkpoint, josta pääsee seuraavaan tasoon. (Pelissä ei vielä voi tallentaa edistymistä).

Pelin voi käynnistää antamalla komentoriville komennon: mvn compile exec:java -Dexec.mainClass=thecursedgame.ui.MyLauncher  .
Testit voi suorittaa komennolla: mvn test jacoco:report tai vain mvn test .
## Dokumentaatio

[Määrittelydokumentti](https://github.com/BigJackz/ot-harjoitustyo/blob/master/Dokumentaatio/maarittelydokumentti.md)

[Tyotuntikirjanpito](https://github.com/BigJackz/ot-harjoitustyo/blob/master/Dokumentaatio/Tyotuntikirjanpito.md) 

[Release 1.0](https://github.com/BigJackz/ot-harjoitustyo/releases/tag/Viikko5)

