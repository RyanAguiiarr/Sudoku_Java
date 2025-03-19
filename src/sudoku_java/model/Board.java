package sudoku_java.model;

import java.util.Collection;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static sudoku_java.model.GameStatusEnum.COMPLETE;
import static sudoku_java.model.GameStatusEnum.NON_STARTED;

public class Board {

    private final List<List<Space>> spaces;

    public Board(final List<List<Space>> spaces) {
        this.spaces = spaces;
    }

    public List<List<Space>> getSpaces() {
        return spaces;
    }

    // ESTADO DO JOGO: MÉTODO QUE RETORNA O ESTADO DO JOGO
    public GameStatusEnum getStatus() {
        if(spaces.stream().flatMap(Collection::stream).noneMatch(space -> !space.isFixed() && nonNull(space.getActual()))) {
            return NON_STARTED;
        }
        return  spaces.stream().flatMap(Collection::stream).anyMatch(space -> isNull(space.getActual())) ? GameStatusEnum.INCOMPLETE : COMPLETE;
    }

    // VERIFICAR ERROS: MÉTODO QUE VERIFICA SE HÁ ERROS NO TABULEIRO
    public boolean hasErrors() {

        if(getStatus() == NON_STARTED){
            return false;
        }
        return spaces.stream().flatMap(Collection::stream).anyMatch(space -> nonNull(space.getActual()) && !space.getActual().equals(space.getExpected()));
    }

    // ALTERAR VALOR: MÉTODO QUE RECEBE A COLUNA, A LINHA E O VALOR A SER ALTERADO
    public boolean changeValue(final int col, final int row, final int value) {
        var space = spaces.get(col).get(row);
        if(space.isFixed()) {
            return false;
        }

        space.setActual(value);
        return true;
    }

    public boolean clearValue(final int col, final int row) {
        var space = spaces.get(col).get(row);
        if(space.isFixed()) {
            return false;
        }

        space.clearSpace();
        return true;
    }

    public void reset(){
        spaces.forEach(c -> c.forEach(Space::clearSpace));
    }

    public boolean gameIsFinished() {
        return getStatus().equals(COMPLETE) && !hasErrors();
    }
}
