package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    @Test
    public void saldoKortinLuodessaOikein() {
        assertEquals("saldo: 0.10", kortti.toString());
    }
    @Test
    public void rahanLataaminenMuuttaaSaldoaOikein() {
        kortti.lataaRahaa(100);
        assertEquals("saldo: 1.10", kortti.toString());
    }
    //@Test Näin hoidettuna testi menee läpi mutta tulos on väärä koska se ilmoittaa kortin saldoksi 0.5 eikä 0.05, mutta jos kortilla on kymmeniä senttejä niin
    // se ilmoittaa sen 0.50 joka on hämäävää joten tein alemman testin selvyyden vuoksi, mutta tässä olisi
    //public void rahanOttaminenToimiiOikein() {
    //    kortti.otaRahaa(5);
    //    assertEquals("saldo: 0.5", kortti.toString());
    //}
    @Test
    public void rahanOttaminenToimiiOikein() {
        Maksukortti k = new Maksukortti(100);
        k.otaRahaa(50);
        assertEquals("saldo: 0.50", k.toString());
    }
    @Test
    public void rahaaEiVoiOttaaYliKortinArvon() {
        kortti.otaRahaa(20);
        assertEquals("saldo: 0.10", kortti.toString());
    }
    @Test
    public void rahanOttaminenPalauttaaTrueJosSeOnnistuu() {
        assertEquals(true, kortti.otaRahaa(5));
    }
    @Test
    public void rahanOttaminenPalauttaaFalseJosSeEiOnnistu() {
        assertEquals(false, kortti.otaRahaa(20));
    }
    @Test
    public void saldonPalautusToimiiOikein() {
	assertEquals(10,kortti.saldo());
    }
}
