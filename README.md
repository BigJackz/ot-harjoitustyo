# HarjoitusTyö
## The Cursed Game
Pelissä voi liikkua käyttämällä näppäimiä A ja D ja hyppiä välilyönnillä.
Pelin hyppy jää ns. "varastoon" ja uuden hypyn saa aina kun laskeutuu vihreälle alustalle tai kerää erikoislaatan.
Pelissä vihreät palikat ovat tasoja joissa voi olla, mustat palikat taas ansoja joihin kuolee.
Pelissä on 9 erilaista kenttää.
Pitäkää hauskaa :D

### Erikoislaatat: 
```
Pinkit pienet laatikot antavat myös hypyn takaisin pelaajalle.
```
```
Vaaleansiniset pienet laatikot taas kasvattavat pelaajan nopeutta tietyksi määräksi liikkumista, nämä kasaantuvat eli jos keräät niitä kaksi kasvaa nopeutesi kahdella joksikin aikaa.
```
```
Sininen keskikokoinen laatikko on checkpoint, josta pääsee seuraavaan tasoon.
```
```
Pieni punainen laatikko antaa korkeamman hypyn joksikin aikaa.
```
```
Violetti noin pelaajan kokoinen laatikko teleporttaa pelaajan jokaisessa kentässä itselleen määriteltyyn paikkaan
```
```
Pieni keltainen laatikko on avain. Keräämällä kaikki avaimet isot ruskeat ovet aukeavat
```
#### huomio jos pelaat peliä 144hz saattaa se tuntua tökkivälle kannattaa vaihtaa näytön virkistystaajuus 60hz jos se vaivaa

### Ohjelman suorittaminen

Pelin voi käynnistää antamalla komentoriville komennon: 
```
mvn compile exec:java -Dexec.mainClass=thecursedgame.ui.MyLauncher
```
Tavallisen testin voi suorittaa komennolla:
```
mvn test
```
Jos haluaa kattavammat tulokset voi käyttää komentoa:
```
mvn test jacoco:report
```

## Dokumentaatio

[Määrittelydokumentti](https://github.com/BigJackz/ot-harjoitustyo/blob/master/Dokumentaatio/maarittelydokumentti.md)

[Tyotuntikirjanpito](https://github.com/BigJackz/ot-harjoitustyo/blob/master/Dokumentaatio/Tyotuntikirjanpito.md) 
[Asennusohje](https://github.com/BigJackz/ot-harjoitustyo/blob/master/Dokumentaatio/asennusohje.md)
[Release 1.0](https://github.com/BigJackz/ot-harjoitustyo/releases/tag/Viikko5)
[Release 2.0](https://github.com/BigJackz/ot-harjoitustyo/releases/tag/viikko6)

