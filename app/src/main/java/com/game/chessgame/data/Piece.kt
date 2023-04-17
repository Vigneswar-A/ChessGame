package com.game.chessgame.data
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class PieceColor{
    BLACK,
    WHITE
}

enum class Pieces{
    KING,
    QUEEN,
    BISHOP,
    KNIGHT,
    ROOK,
    PAWN
}

enum class PieceSelection{
    UNSELECTED,
    SELECTED,
    VULNERABLE
}

class Piece(piece: Pieces, val color: PieceColor, pos: Pair<Int, Int>){
    var piece by mutableStateOf(piece)
    var pos by mutableStateOf(pos)
    var selection by mutableStateOf(PieceSelection.UNSELECTED)
    var flip by mutableStateOf(false)
    var isDead by mutableStateOf(false)
}