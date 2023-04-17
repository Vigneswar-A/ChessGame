package com.game.chessgame.ui.theme
import androidx.lifecycle.ViewModel
import com.game.chessgame.data.Piece
import com.game.chessgame.data.PieceColor
import com.game.chessgame.data.PieceSelection
import com.game.chessgame.data.Pieces
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

val initialArrangement = listOf(
    Piece(Pieces.ROOK, color = PieceColor.BLACK, 0 to 0),
    Piece(Pieces.KNIGHT, color = PieceColor.BLACK, 0 to 1),
    Piece(Pieces.BISHOP, color = PieceColor.BLACK, 0 to 2),
    Piece(Pieces.QUEEN, color = PieceColor.BLACK, 0 to 3),
    Piece(Pieces.KING, color = PieceColor.BLACK, 0 to 4),
    Piece(Pieces.BISHOP, color = PieceColor.BLACK,0 to 5),
    Piece(Pieces.KNIGHT, color = PieceColor.BLACK, 0 to 6),
    Piece(Pieces.ROOK, color = PieceColor.BLACK, 0 to 7),

    Piece(Pieces.PAWN, color = PieceColor.BLACK, 1 to 0),
    Piece(Pieces.PAWN, color = PieceColor.BLACK, 1 to 1),
    Piece(Pieces.PAWN, color = PieceColor.BLACK, 1 to 2),
    Piece(Pieces.PAWN, color = PieceColor.BLACK, 1 to 3),
    Piece(Pieces.PAWN, color = PieceColor.BLACK, 1 to 4),
    Piece(Pieces.PAWN, color = PieceColor.BLACK, 1 to 5),
    Piece(Pieces.PAWN, color = PieceColor.BLACK, 1 to 6),
    Piece(Pieces.PAWN, color = PieceColor.BLACK, 1 to 7),

    Piece(Pieces.ROOK, color = PieceColor.WHITE, 7 to 0),
    Piece(Pieces.KNIGHT, color = PieceColor.WHITE, 7 to 1),
    Piece(Pieces.BISHOP, color = PieceColor.WHITE, 7 to 2),
    Piece(Pieces.QUEEN, color = PieceColor.WHITE, 7 to 3),
    Piece(Pieces.KING, color = PieceColor.WHITE, 7 to 4),
    Piece(Pieces.BISHOP, color = PieceColor.WHITE, 7 to 5),
    Piece(Pieces.KNIGHT, color = PieceColor.WHITE, 7 to 6),
    Piece(Pieces.ROOK, color = PieceColor.WHITE, 7 to 7),

    Piece(Pieces.PAWN, color = PieceColor.WHITE, 6 to 0),
    Piece(Pieces.PAWN, color = PieceColor.WHITE, 6 to 1),
    Piece(Pieces.PAWN, color = PieceColor.WHITE, 6 to 2),
    Piece(Pieces.PAWN, color = PieceColor.WHITE, 6 to 3),
    Piece(Pieces.PAWN, color = PieceColor.WHITE, 6 to 4),
    Piece(Pieces.PAWN, color = PieceColor.WHITE, 6 to 5),
    Piece(Pieces.PAWN, color = PieceColor.WHITE, 6 to 6),
    Piece(Pieces.PAWN, color = PieceColor.WHITE, 6 to 7),
)

object SpecialMoveManager{
    var whiteLeftRookMoved = false
    var whiteRightRookMoved = false
    var blackLeftRookMoved = false
    var blackRightRookMoved = false
    var blackKingMoved = false
    var whiteKingMoved = false
}

class BoardViewModel : ViewModel() {
    var pieces: MutableList<Piece> = initialArrangement.toMutableList()
    private val piecePos = HashMap<Pair<Int, Int>, Piece>()
    private var selected: Piece? = null
    private val _uiState = MutableStateFlow(BoardUiState(hashSetOf()))
    val uiState = _uiState.asStateFlow()
    private val vulnerable = mutableListOf<Piece>()
    private var currentPlayer: PieceColor = PieceColor.WHITE
    private val valid = hashSetOf<Pair<Int, Int>>()

    init {
        reset()
    }

    private fun reset() {
        pieces = initialArrangement.toMutableList()
        for(piece in pieces){
            piecePos[piece.pos] = piece
        }
        currentPlayer = PieceColor.WHITE
    }

    private fun updateValid(validPos: HashSet<Pair<Int, Int>> = hashSetOf()){
        _uiState.update {currentState -> currentState.copy(validPos = validPos) }
    }

    private fun isCheckMate(player: PieceColor = currentPlayer): Boolean{
        val temp: Map<Pair<Int, Int>, Piece> = piecePos.toMap()
        for((pos, piece) in temp.entries){
            if(piece.color == player){
                val tempValid = hashSetOf<Pair<Int, Int>>()
                getValidMoves(piece, tempValid, mutableListOf())
                for(newPos in tempValid){
                    val old = piecePos[newPos]
                    piecePos.remove(pos)
                    piecePos[newPos] = piece
                    if(!isCheck(player)){
                        if(old!= null) piecePos[newPos] = old else piecePos.remove(newPos)
                        piecePos[pos] = piece
                        return false
                    }
                    if(old != null) piecePos[newPos] = old else piecePos.remove(newPos)
                    piecePos[pos] = piece
                }
            }
        }
        return true
    }

    private fun switchPlayer(){
        currentPlayer = when(currentPlayer){
            PieceColor.WHITE -> PieceColor.BLACK
            PieceColor.BLACK -> PieceColor.WHITE
        }
        for(piece in pieces){
            piece.flip = when(currentPlayer){
                PieceColor.WHITE -> false
                PieceColor.BLACK -> true
            }
        }
    }

    private fun isCheck(player: PieceColor = currentPlayer): Boolean{
        lateinit var kingPos: Pair<Int, Int>
        for((pos, piece) in piecePos.entries){
            if(piece.color == player && piece.piece == Pieces.KING){
                kingPos = pos
                break
            }
        }
        for((_, piece) in piecePos.entries){
            if(piece.color == player)
                continue
            val tempValid = hashSetOf<Pair<Int, Int>>()
            getValidMoves(piece, tempValid, mutableListOf())
            if(tempValid.contains(kingPos))
                return true
        }
        return false
    }

    private fun getValidMoves(
        piece: Piece,
        validMoves: HashSet<Pair<Int, Int>> = valid,
        vulnerablePieces: MutableList<Piece> = vulnerable
    ){
        when(piece.piece){
            Pieces.PAWN -> {
                val (currX, currY) = piece.pos

                if(currX == 6 && piece.color == PieceColor.WHITE
                    && piecePos[currX - 2 to currY] == null
                    && piecePos[currX - 1 to currY] == null)
                    validMoves.add(currX-2 to currY)

                if(currX == 1 && piece.color == PieceColor.BLACK
                    && piecePos[currX + 2 to currY] == null
                    && piecePos[currX + 1 to currY] == null)
                    validMoves.add(currX+2 to currY)

                val x = when(piece.color){
                    PieceColor.WHITE -> -1
                    PieceColor.BLACK -> 1
                }
                if(currX + x in 0..7
                    && currY  in 0..7
                    && piecePos[currX + x to currY] == null)
                    validMoves.add(currX+x to currY)

                for(y in listOf(-1, 1))
                    if(piecePos[currX + x to currY + y] != null
                        && piecePos[currX + x to currY + y]?.color != piece.color) {
                        vulnerablePieces.add(piecePos[currX + x to currY + y]!!)
                        validMoves.add(currX + x to currY + y)
                    }

            }

            Pieces.BISHOP-> {
                for((x, y) in listOf(-1 to -1, -1 to 1, 1 to -1, 1 to 1)) {
                    var (currX, currY) = piece.pos
                    while (currX + x in 0..7
                        && currY + y in 0..7
                        && piecePos[currX + x to currY + y]?.color != piece.color
                    ) {
                        validMoves.add((currX + x to currY + y))
                        if(piecePos[currX + x to currY + y] != null) {
                            vulnerablePieces.add(piecePos[currX + x to currY + y]!!)
                            break
                        }
                        currX += x
                        currY += y
                    }
                }
            }

            Pieces.KNIGHT->{
                val (currX, currY) = piece.pos
                for((x, y) in listOf(-2 to -1, -2 to 1, -1 to -2, -1 to 2, 1 to -2, 1 to 2, 2 to 1, 2 to -1)){
                    if(currX + x in 0..7
                        && currY + y in 0..7
                        && piecePos[currX + x to currY + y]?.color != piece.color) {
                        validMoves.add(currX + x to currY + y)
                        if(piecePos[currX + x to currY + y] != null)
                            vulnerablePieces.add(piecePos[currX + x to currY + y]!!)
                    }
                }
            }

            Pieces.KING->{
                val (currX, currY) = piece.pos
                for((x, y) in listOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)){
                    if(currX + x in 0..7
                        && currY + y in 0..7
                        && piecePos[currX + x to currY + y]?.color != piece.color) {
                        validMoves.add(currX + x to currY + y)
                        if (piecePos[currX + x to currY + y] != null)
                            vulnerablePieces.add(piecePos[currX + x to currY + y]!!)
                    }
                }

                if(piece.color == PieceColor.WHITE
                    && !SpecialMoveManager.whiteLeftRookMoved
                    && !SpecialMoveManager.whiteKingMoved
                    && piecePos[7 to 1] == null
                    && piecePos[7 to 2] == null
                    && piecePos[7 to 3] == null){
                    validMoves.add(7 to 2)
                }

                if(piece.color == PieceColor.WHITE
                    && !SpecialMoveManager.whiteRightRookMoved
                    && !SpecialMoveManager.whiteKingMoved
                    && piecePos[7 to 5] == null
                    && piecePos[7 to 6] == null){
                    validMoves.add(7 to 6)
                }

                if(piece.color == PieceColor.BLACK
                    && !SpecialMoveManager.blackRightRookMoved
                    && !SpecialMoveManager.blackKingMoved
                    && piecePos[0 to 1] == null
                    && piecePos[0 to 2] == null
                    && piecePos[0 to 3] == null){
                    validMoves.add(0 to 2)
                }

                if(piece.color == PieceColor.BLACK
                    && !SpecialMoveManager.blackLeftRookMoved
                    && !SpecialMoveManager.blackKingMoved
                    && piecePos[0 to 5] == null
                    && piecePos[0 to 6] == null){
                    validMoves.add(0 to 6)
                }
            }

            Pieces.ROOK->{
                for((x, y) in listOf(-1 to 0, 0 to -1, 0 to 1, 1 to 0)) {
                    var (currX, currY) = piece.pos
                    while (currX + x in 0..7
                        && currY + y in 0..7
                        && piecePos[currX + x to currY + y]?.color != piece.color) {
                        validMoves.add((currX + x to currY + y))
                        if(piecePos[currX + x to currY + y] != null) {
                            vulnerablePieces.add(piecePos[currX + x to currY + y]!!)
                            break
                        }
                        currX += x
                        currY += y
                    }
                }
            }

            Pieces.QUEEN->{
                for((x, y) in listOf(-1 to -1, -1 to 1, 1 to -1, 1 to 1, -1 to 0, 0 to -1, 0 to 1, 1 to 0)) {
                    var (currX, currY) = piece.pos
                    while (currX + x in 0..7
                        && currY + y in 0..7
                        && piecePos[currX + x to currY + y]?.color != piece.color) {
                        validMoves.add((currX + x to currY + y))
                        if(piecePos[currX + x to currY + y] != null) {
                            vulnerablePieces.add(piecePos[currX + x to currY + y]!!)
                            break
                        }
                        currX += x
                        currY += y
                    }
                }
            }
        }
    }

    fun onCellClicked(r: Int, c: Int){
        if(selected == null) {
            selected = piecePos[r to c]
            if (selected == null || selected!!.color != currentPlayer) {
                selected = null
                return
            }

            selected!!.selection = PieceSelection.SELECTED
            val tempValid = hashSetOf<Pair<Int, Int>>()
            getValidMoves(selected!!, tempValid)
            valid.clear()
            vulnerable.clear()

            if(isCheck()) {
                if (selected!!.piece == Pieces.KING
                    && selected!!.color == PieceColor.WHITE
                    && selected!!.pos == 7 to 4
                ) {
                    tempValid.remove(7 to 2)
                    tempValid.remove(7 to 6)
                } else if (selected!!.piece == Pieces.KING
                    && selected!!.color == PieceColor.BLACK
                    && selected!!.pos == 0 to 4
                ) {
                    tempValid.remove(0 to 2)
                    tempValid.remove(0 to 6)
                }
            }
            // verify if move would cause a check
            for(pos in tempValid){
                val temp = piecePos[pos]
                piecePos.remove(selected!!.pos)
                piecePos[pos] = selected!!
                if(!isCheck()){
                    valid.add(pos)
                    if(temp != null)
                        vulnerable.add(temp)
                }
                if(temp != null)
                    piecePos[pos] = temp
                else
                    piecePos.remove(pos)
                piecePos[selected!!.pos] = selected!!
            }

            updateValid(valid)
            for(piece in vulnerable)
                piece.selection = PieceSelection.VULNERABLE
        }
        else {
            if(uiState.value.validPos.contains(r to c)) {
                piecePos[r to c]?.isDead = true
                piecePos.remove(selected!!.pos)

                //Pawn Promotion
                if((r == 0 || r == 7) && selected!!.piece == Pieces.PAWN){
                    selected!!.piece = Pieces.QUEEN
                }

                // castling
                for(x in listOf(0, 7)){
                    if(selected!!.piece == Pieces.KING
                        && selected!!.pos == x to 4){
                        if(r to c == x to 2)
                        {
                            piecePos[x to 3] = piecePos[x to 0]!!
                            piecePos.remove(x to 0)
                            piecePos[x to 3]!!.pos = x to 3
                        }
                        else if(r to c == x to 6){
                            piecePos[x to 5] = piecePos[x to 7]!!
                            piecePos.remove(x to 7)
                            piecePos[x to 5]!!.pos = x to 5
                        }
                    }
                }

                selected!!.pos = r to c
                piecePos[r to c] = selected!!

                if(selected!!.piece == Pieces.ROOK)
                    when(selected!!.pos) {
                        7 to 0 -> SpecialMoveManager.whiteLeftRookMoved = true
                        7 to 7 -> SpecialMoveManager.whiteRightRookMoved = true
                        0 to 7 -> SpecialMoveManager.blackLeftRookMoved = true
                        0 to 0 -> SpecialMoveManager.blackRightRookMoved = true
                    }
                else if(selected!!.piece == Pieces.KING)
                    when(selected!!.pos){
                        7 to 4 -> SpecialMoveManager.whiteKingMoved = true
                        0 to 4 -> SpecialMoveManager.blackKingMoved = true
                    }


                switchPlayer()
                if(isCheckMate()) {
                    println("Game Over")
                    // TODO
                }
            }
            updateValid()
            selected!!.selection = PieceSelection.UNSELECTED
            selected = null
            for(piece in vulnerable)
                piece.selection = PieceSelection.UNSELECTED
            vulnerable.clear()
        }
    }
}