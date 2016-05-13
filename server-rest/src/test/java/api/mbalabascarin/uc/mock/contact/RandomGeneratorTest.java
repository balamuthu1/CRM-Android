package api.mbalabascarin.uc.mock.contact;

import miage.pds.api.mbalabascarin.uc.mock.contact.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RandomGeneratorTest {
	
	RandomGenerator rg;
	
	@Before
	public void initTest(){
		System.out.println("Tests launched...");
		rg = new RandomGenerator();
	}
	
	@Test
	public void testGeneratedPhoneNumberIsString(){
		assertEquals(true, rg.generatePhoneNumber().getClass().equals(String.class));
	}
	
	@Test
	public void testIsNumberCorrect(){
		Boolean correct = false;
		String number = rg.generatePhoneNumber();
		correct = rg.isNumberCorrect(number);
		assertTrue(correct);
	}
	@Test
	public void testIsNumberNotCorrect(){
		Boolean correct = false;
		String number = rg.generatePhoneNumber();
		correct = rg.isNumberCorrect(number+"1");
		assertFalse(correct);
	}
	@Test
	public void testIsNumberNotCorrectWithStartNumberCheck(){
		Boolean correct = false;
		
		correct = rg.isNumberCorrect("00 12 13 14 15");
		assertFalse(correct);
	}
	
	

}
