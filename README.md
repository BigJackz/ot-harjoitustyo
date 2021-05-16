# HarjoitusTyö
## The Cursed Game
Pelissä voi liikkua käyttämällä näppäimiä A ja D ja hyppiä välilyönnillä.
Pelin hyppy jää ns. "varastoon" ja uuden hypyn saa aina kun laskeutuu vihreälle alustalle tai kerää erikoislaatan.
Pelissä vihreät palikat ovat tasoja joissa voi olla, mustat palikat taas ansoja joihin kuolee.
Pelissä on 9 erilaista kenttää.
Pitäkää hauskaa :D

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


