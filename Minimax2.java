/* 
    Thuật toán Minimax - cắt tỉa Alpha Beta

    Chức năng các hàm:
    - possibleMoves() : trả về danh sách các vị trí, tương ứng với các nước đi khả dĩ
    (vì dùng vét cạn nên ta chỉ xét các nước đi có quân bên cạnh mà thôi)
    - heuristic() : trả về điểm của bot so với player
    - minimax() : chạy thuật toán Minimax, kèm cắt tỉa alpha-beta
    - best_move() : lựa chọn trong các nước đi khả dĩ, nước nào có kết quả khả quan nhất
*/

package Caro;

import java.util.ArrayList;
import java.util.List;

public class Minimax2 {

    private static int n = 15;
	
    private static List<int[]> possibleMoves(int[][] position) {
        List<int[]> possibleMoves = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if(position[i][j] > 0) continue;

                if(i > 0) {
                    if(j > 0) {
                        if(position[i-1][j-1] > 0 || position[i][j-1] > 0) {
                            int[] move = {i, j};
                            possibleMoves.add(move);
                            continue;
                        }
                    }
                    if(j < n-1) {
                        if(position[i-1][j+1] > 0 || position[i][j+1] > 0) {
                            int[] move = {i, j};
                            possibleMoves.add(move);
                            continue;
                        }
                    }
                    if(position[i-1][j] > 0) {
                        int[] move = {i, j};
                        possibleMoves.add(move);
                        continue;
                    }
                }

                if(i < n-1) {
                    if(j > 0) {
                        if(position[i+1][j-1] > 0 || position[i][j-1] > 0) {
                            int[] move = {i, j};
                            possibleMoves.add(move);
                            continue;
                        }
                    }
                    if(j < n-1) {
                        if(position[i+1][j+1] > 0 || position[i][j+1] > 0) {
                            int[] move = {i, j};
                            possibleMoves.add(move);
                            continue;
                        }
                    }
                    if(position[i+1][j] > 0) {
                        int[] move = {i, j};
                        possibleMoves.add(move);
                        continue;
                    }
                }
            }
        }
        return possibleMoves;
    }
    
    private static double heuristic(int[][] position, boolean player1Turn) {
        
        double playerScore = Heuristic2.getScore(position, true, player1Turn);
        double botScore = Heuristic2.getScore(position, false, player1Turn);

        if(playerScore == 0) playerScore = 1.0;

        return botScore / playerScore;
    }

    private static double minimax(int[][] position, int depth, double alpha, double beta, boolean player1Turn) {
        if(depth == 0 || possibleMoves(position).size() == 0) {
            return heuristic(position, !player1Turn);
        }
        if(player1Turn) {
            double maxValue = -1;
            for(int i = 0; i < possibleMoves(position).size(); i++) {
                int[][] current_board = new int[n][n];
                for(int j = 0; j < n; j++)
                    for(int k = 0; k < n; k++)
                        current_board[j][k] = position[j][k];

                current_board[possibleMoves(position).get(i)[0]][possibleMoves(position).get(i)[1]] = 1;
                double value = minimax(current_board, depth-1, alpha, beta, false);
                alpha = Math.max(alpha, value);
                if(value >= beta) return value;
                maxValue = Math.max(value, maxValue);
                
            }
            return maxValue;
        }
        else {
            double minValue = 10000000;
            for(int i = 0; i < possibleMoves(position).size(); i++) {
                int[][] current_board = new int[n][n];
                for(int j = 0; j < n; j++)
                    for(int k = 0; k < n; k++)
                        current_board[j][k] = position[j][k];

                current_board[possibleMoves(position).get(i)[0]][possibleMoves(position).get(i)[1]] = 2;
                double value = minimax(current_board, depth-1, alpha, beta, true);
                beta = Math.min(beta, value);
                if(value <= alpha) return value;
                minValue = Math.min(value, minValue);
    
            }
            return minValue;
        }
    }

    public static int[] best_move(int[][] position, boolean player1Turn) {
        int[] location = new int[2];
        double value = -1;

        for(int i = 0; i < possibleMoves(position).size(); i++) {
            int[][] current_board = new int[n][n];
            for(int j = 0; j < n; j++)
                for(int k = 0; k < n; k++)
                    current_board[j][k] = position[j][k];
                    
            current_board[possibleMoves(position).get(i)[0]][possibleMoves(position).get(i)[1]] = 2;
            
            if(minimax(current_board, 2, -1, Heuristic.winScore, false) >= value) {
                value = minimax(current_board, 2, -1, Heuristic.winScore, false);
                location = possibleMoves(position).get(i);
            }
        }
        return location;
    }
}
