package edu.oakland.tictactoe;

public class ApplicationUtil {

    public static String encodeTextSMS(String playerName, String selectedSym, String messageType){
        //Encode message
        StringBuilder sb = new StringBuilder();
        sb.append("STicTacToe").append(" ").append("TIC-TAC-TOE").append(" ").append(messageType).
                append(" ").append(playerName).append(" ").append(selectedSym);
        return sb.toString();
    }
}
