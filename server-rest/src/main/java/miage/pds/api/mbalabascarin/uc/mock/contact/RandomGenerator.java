package miage.pds.api.mbalabascarin.uc.mock.contact;

import java.util.Random;

public class RandomGenerator {
	
	public String generatePhoneNumber(){
		
		 int num1, num2, num3, num4, num5, num6, num7, num8;
	        
	        
	        Random generator = new Random();
	        
	        //Area code number; Will not print 8 or 9
	        num1 = generator.nextInt(7) + 1; //add 1 so there is no 0 to begin  
	        num2 = generator.nextInt(8); //randomize to 8 becuase 0 counts as a number in the generator
	        num3 = generator.nextInt(8);
	        num4 = generator.nextInt(8);
	        num5 = generator.nextInt(8);
	        num6 = generator.nextInt(8);
	        num7 = generator.nextInt(8);
	        num8 = generator.nextInt(8);
	        
	        String phoneNumber = "06"+ " "+ num1+num2+" "+num3+num4+" "+num5+num6+" "+num7+num8;
	        
		return phoneNumber;
	}
	
	public boolean isNumberCorrect(String phone){
		char[] letters = phone.toCharArray();
		
		if((phone.startsWith("01")||phone.startsWith("06")|| phone.startsWith("07")) && letters.length == 14){
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RandomGenerator rg = new RandomGenerator();
		for(int i=0; i<1266 ; i++){
			System.out.println(rg.generatePhoneNumber());
		}
		

	}


}
