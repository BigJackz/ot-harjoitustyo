package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.*;

public class KassapaateTest {
	Kassapaate kassapaate;
	Maksukortti maksukortti;
	Maksukortti koyhaMaksukortti;
	@Before
	public void setUp() {
		kassapaate = new Kassapaate();
		maksukortti = new Maksukortti(1000);
		koyhaMaksukortti = new Maksukortti(10);
	}
	@Test
	public void rahaaOikeaMaaraAlussa() {
		assertEquals(100000, kassapaate.kassassaRahaa());
	}
	@Test
	public void maukkautaLounaitaMyytyOikeaMaara() {
		assertEquals(0, kassapaate.maukkaitaLounaitaMyyty());
	}
	@Test
	public void edullisiaLounaitaMyytyOikeaMaara() {
		assertEquals(0, kassapaate.edullisiaLounaitaMyyty());
	}
	@Test
	public void syoMaukkaastiKateisellaNostaaKassanRahamaaraa() {
		kassapaate.syoMaukkaasti(400);
		assertEquals(100400, kassapaate.kassassaRahaa());
	}
	@Test
	public void syoMaukkaastiKateisellaVaihtorahaaOikeaMaara() {
		assertEquals(200, kassapaate.syoMaukkaasti(600));
	}
	@Test
	public void syoMaukkaastiKasvattaaMaukkaidenMaaraaJosRahaaOnTarpeeksi() {
		kassapaate.syoMaukkaasti(400);
		assertEquals(1, kassapaate.maukkaitaLounaitaMyyty());
	}
	@Test
	public void syoMaukkaastiEiKasvataMaukkaidenMaaraaJosRahaaEiOleTarpeeksi() {
		kassapaate.syoMaukkaasti(300);
		assertEquals(0, kassapaate.maukkaitaLounaitaMyyty());
	}
	@Test
	public void syoMaukkaastiEiMuutaKassanRahamaaraaJosRahaaEiOleTarpeeksi() {
		kassapaate.syoMaukkaasti(300);
		assertEquals(100000, kassapaate.kassassaRahaa());
	}
	@Test
	public void syoMaukkaastiPalauttaaKaikkiRahatJosRahaaEiTarpeeksi() {
		assertEquals(300, kassapaate.syoMaukkaasti(300));
	}
//-------------------------------------------------------------------------------------------------------------------
	@Test
	public void syoEdullisestiKateisellaNostaaKassanRahamaaraa() {
		kassapaate.syoEdullisesti(400);
		assertEquals(100240, kassapaate.kassassaRahaa());
	}
	@Test
	public void syoEdullisestiKateisellaVaihtorahaaOikeaMaara() {
		assertEquals(360, kassapaate.syoEdullisesti(600));
	}
	@Test
	public void syoEdullisestiKasvattaaEdullistenMaaraaJosRahaaOnTarpeeksi() {
		kassapaate.syoEdullisesti(400);
		assertEquals(1, kassapaate.edullisiaLounaitaMyyty());
	}
	@Test
	public void syoEdullisestiEiKasvataEdullistenMaaraaJosRahaaEiOleTarpeeksi() {
		kassapaate.syoEdullisesti(100);
		assertEquals(0, kassapaate.edullisiaLounaitaMyyty());
	}
	@Test
	public void syoEdullisestiEiMuutaKassanRahamaaraaJosRahaaEiOleTarpeeksi() {
		kassapaate.syoEdullisesti(100);
		assertEquals(100000, kassapaate.kassassaRahaa());
	}
	@Test
	public void syoEdullisestiPalauttaaKaikkiRahatJosRahaaEiTarpeeksi() {
		assertEquals(100, kassapaate.syoEdullisesti(100));
	}
//------------------------------------------------------------------------------------------------------------
	@Test
	public void syoMaukkaastiKortillaVahentaaKortinArvoa() {
		kassapaate.syoMaukkaasti(maksukortti);
		assertEquals("saldo: 6.0", maksukortti.toString());
	}
	@Test
	public void syoMaukkaastiKortillaPalauttaaTrueJosRahaaTarpeeksi() {
		assertEquals(true, kassapaate.syoMaukkaasti(maksukortti));
	}
	@Test
	public void syoMaukkaastiKortillaKasvattaaMaukkaidenMaaraaJosRahaaTarpeeksi() {
		kassapaate.syoMaukkaasti(maksukortti);
		assertEquals(1, kassapaate.maukkaitaLounaitaMyyty());
	}
	@Test
	public void syoMaukkaastiKortillaEiKasvataMaukkaidenMaaraaJosEiTarpeeksiRahaa() {
		kassapaate.syoMaukkaasti(koyhaMaksukortti);
		assertEquals(0, kassapaate.maukkaitaLounaitaMyyty());
	}
	@Test
	public void syoMaukkaastiKortillaEiMuutaKortinRahamaaraaJosEiTarpeeksiRahaa() {
		kassapaate.syoMaukkaasti(koyhaMaksukortti);
		assertEquals("saldo: 0.10", koyhaMaksukortti.toString());
	}
	@Test
	public void syoMaukkaastiKortillaPaulauttaaFalseJosEiTarpeeksiRahaa() {
		assertEquals(false, kassapaate.syoMaukkaasti(koyhaMaksukortti));
	}
	@Test
	public void syoMaukkaastiKortillaEiKasvataKassanRahamaaraa() {
		kassapaate.syoMaukkaasti(maksukortti);
		assertEquals(100000, kassapaate.kassassaRahaa());
	}
//---------------------------------------------------------------------------------------------------------------
	@Test
	public void syoEdullisestiKortillaVahentaaKortinArvoa() {
		kassapaate.syoEdullisesti(maksukortti);
		assertEquals("saldo: 7.60", maksukortti.toString());
	}
	@Test
	public void syoEdullisestiKortillaPalauttaaTrueJosRahaaTarpeeksi() {
		assertEquals(true, kassapaate.syoEdullisesti(maksukortti));
	}
	@Test
	public void syoEdullisestiKortillaKasvattaaEdullistenMaaraaJosRahaaTarpeeksi() {
		kassapaate.syoEdullisesti(maksukortti);
		assertEquals(1, kassapaate.edullisiaLounaitaMyyty());
	}
	@Test
	public void syoEdullisestiKortillaEiKasvataEdullistenMaaraaJosEiTarpeeksiRahaa() {
		kassapaate.syoEdullisesti(koyhaMaksukortti);
		assertEquals(0, kassapaate.edullisiaLounaitaMyyty());
	}
	@Test
	public void syoEdullisestiKortillaEiMuutaKortinRahamaaraaJosEiTarpeeksiRahaa() {
		kassapaate.syoEdullisesti(koyhaMaksukortti);
		assertEquals("saldo: 0.10", koyhaMaksukortti.toString());
	}
	@Test
	public void syoEdullisestiKortillaPaulauttaaFalseJosEiTarpeeksiRahaa() {
		assertEquals(false, kassapaate.syoEdullisesti(koyhaMaksukortti));
	}
	@Test
	public void syoEdullisestiKortillaEiKasvataKassanRahamaaraa() {
		kassapaate.syoEdullisesti(maksukortti);
		assertEquals(100000, kassapaate.kassassaRahaa());
	}
	@Test
	public void rahaaLadattaessaKortinSaldoMuuttuu() {
		kassapaate.lataaRahaaKortille(maksukortti, 100);
		assertEquals("saldo: 11.0", maksukortti.toString());
	}
	@Test
	public void rahaaLadattaessaKassanRahamaaraKasvaa() {
		kassapaate.lataaRahaaKortille(maksukortti, 100);
		assertEquals(100100, kassapaate.kassassaRahaa());
	}
	@Test
	public void rahaaLadattaessaEiTapahduMitaanJosSummaEiOlePositiivinen() {
		kassapaate.lataaRahaaKortille(maksukortti, -20);
		assertEquals(100000, kassapaate.kassassaRahaa());
	}
}
