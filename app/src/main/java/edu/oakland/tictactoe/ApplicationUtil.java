package edu.oakland.tictactoe;

public class ApplicationUtil {

    public static String encodeTextSMS(String playerName, String selectedSym, String messageType, int dataCell){
        //Encode message
        StringBuilder sb = new StringBuilder();
        sb.append("STicTacToe").append(" ").append("TIC-TAC-TOE").append(" ").append(messageType).
                append(" ").append(playerName).append(" ").append(selectedSym).append(" ").append(dataCell);
        return sb.toString();
    }
}
