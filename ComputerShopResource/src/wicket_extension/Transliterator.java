package wicket_extension;

/** класс, который служит для переопределения русских букв в латиницу */
public class Transliterator {
    private static final String[] charTable = new String[81];

    private static final char START_CHAR = 'Ё';

    static {
        charTable['А'- START_CHAR] = "A";
        charTable['Б'- START_CHAR] = "B";
        charTable['В'- START_CHAR] = "V";
        charTable['Г'- START_CHAR] = "G";
        charTable['Д'- START_CHAR] = "D";
        charTable['Е'- START_CHAR] = "E";
        charTable['Ё'- START_CHAR] = "E";
        charTable['Ж'- START_CHAR] = "ZH";
        charTable['З'- START_CHAR] = "Z";
        charTable['И'- START_CHAR] = "I";
        charTable['Й'- START_CHAR] = "I";
        charTable['К'- START_CHAR] = "K";
        charTable['Л'- START_CHAR] = "L";
        charTable['М'- START_CHAR] = "M";
        charTable['Н'- START_CHAR] = "N";
        charTable['О'- START_CHAR] = "O";
        charTable['П'- START_CHAR] = "P";
        charTable['Р'- START_CHAR] = "R";
        charTable['С'- START_CHAR] = "S";
        charTable['Т'- START_CHAR] = "T";
        charTable['У'- START_CHAR] = "U";
        charTable['Ф'- START_CHAR] = "F";
        charTable['Х'- START_CHAR] = "H";
        charTable['Ц'- START_CHAR] = "C";
        charTable['Ч'- START_CHAR] = "CH";
        charTable['Ш'- START_CHAR] = "SH";
        charTable['Щ'- START_CHAR] = "SCH";
        charTable['Ъ'- START_CHAR] = "'";
        charTable['Ы'- START_CHAR] = "Y";
        charTable['Ь'- START_CHAR] = "'";
        charTable['Э'- START_CHAR] = "E";
        charTable['Ю'- START_CHAR] = "YU"; // U
        charTable['Я'- START_CHAR] = "YA"; // Я

        for (int i = 0; i < charTable.length; i++) {
            char idx = (char)((char)i + START_CHAR);
            char lower = new String(new char[]{idx}).toLowerCase().charAt(0);
            if (charTable[i] != null) {
                charTable[lower - START_CHAR] = charTable[i].toLowerCase();
            }
        }
    }


    /**
     * @param text исходный текст с русскими символами
     * @return результат
     */
    public static synchronized String toTranslit(String text) {
        char charBuffer[] = text.toCharArray();
        StringBuilder sb = new StringBuilder(text.length());
        char symbol;
        char oldChar=' ';
        for(int counter=0;counter<charBuffer.length;counter++){
        	symbol=charBuffer[counter];
            int i = symbol - START_CHAR;
            if (i>=0 && i<charTable.length) {
            	if(oldChar==' '){
            		// word begin
                    String replace = charTable[i];
                    if(replace.equals("y")){
                    	replace="i";
                    }
                    if(replace.equals("Y")){
                    	replace="I";
                    }
                    if(replace.equals("YU")){
                    	replace="IU";
                    }
                    if(replace.equals("yu")){
                    	replace="iu";
                    }
                    if(replace.equals("YA")){
                    	replace="IA";
                    }
                    if(replace.equals("ya")){
                    	replace="ia";
                    }
                    sb.append(replace == null ? symbol : replace);
            	}else{
                    String replace = charTable[i];
                    sb.append(replace == null ? symbol : replace);
            	}
            }
            else {
                sb.append(symbol);
            }
            oldChar=symbol;
        }
        return sb.toString();
    }


    



}
