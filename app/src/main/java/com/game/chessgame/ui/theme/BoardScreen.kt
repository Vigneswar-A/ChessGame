package com.game.chessgame.ui.theme
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.game.chessgame.R
import com.game.chessgame.data.Piece
import com.game.chessgame.data.PieceColor
import com.game.chessgame.data.PieceSelection
import com.game.chessgame.data.Pieces

const val BOARD_SIZE = 320

@Composable
fun Board(viewModel: BoardViewModel){
    Box(
        modifier = Modifier
            .size(BOARD_SIZE.dp + 10.dp)
            .border(5.dp, boardBorder)
    ) {
        Box(
            modifier = Modifier
                .size(BOARD_SIZE.dp)
                .offset(5.dp, 5.dp)
        ) {
            BoardBackground(viewModel)
            ValidMoves(viewModel)
            for (piece in viewModel.pieces) {
                Piece(piece)
            }
        }
    }
}

@Composable
fun BoardBackground(viewModel: BoardViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        for (i in 0..7) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .height((BOARD_SIZE / 8).dp)) {
                for (j in 0..7) {
                    Box(modifier = Modifier
                        .size((BOARD_SIZE / 8).dp)
                        .background(if ((i + j) % 2 == 0) chessBlack else chessWhite)
                        .clickable { viewModel.onCellClicked(i, j) }
                    )
                    }
                }
            }
    }
}

@Composable
fun ValidMoves(viewModel: BoardViewModel){
    val uiState by viewModel.uiState.collectAsState()
    for((i,j) in uiState.validPos)
        Canvas(modifier = Modifier
            .size((BOARD_SIZE / 8).dp)
            .offset((BOARD_SIZE / 8).dp*j, (BOARD_SIZE / 8).dp*i)
        ){
            drawCircle(
                color = chessValid,
                radius = 4.dp.toPx(),
            )
        }
}

@Composable
fun Piece(piece: Piece){
    if (piece.isDead)
        return
    val posR by animateDpAsState(
        targetValue = (BOARD_SIZE/8).dp * piece.pos.second,
        animationSpec = pieceMovementAnimation
    )
    val posC by animateDpAsState(
        targetValue = (BOARD_SIZE/8).dp * piece.pos.first,
        animationSpec = pieceMovementAnimation
    )

    Image(
        painter = when(piece.selection) {
                PieceSelection.UNSELECTED -> painterResource(
                    when(piece.color){
                        PieceColor.WHITE -> when(piece.piece){
                            Pieces.KING -> R.drawable.white_king
                            Pieces.QUEEN -> R.drawable.white_queen
                            Pieces.BISHOP -> R.drawable.white_bishop
                            Pieces.KNIGHT -> R.drawable.white_knight
                            Pieces.ROOK -> R.drawable.white_rook
                            Pieces.PAWN -> R.drawable.white_pawn
                        }

                        PieceColor.BLACK -> when(piece.piece){
                            Pieces.KING -> R.drawable.black_king
                            Pieces.QUEEN -> R.drawable.black_queen
                            Pieces.BISHOP -> R.drawable.black_bishop
                            Pieces.KNIGHT -> R.drawable.black_knight
                            Pieces.ROOK -> R.drawable.black_rook
                            Pieces.PAWN -> R.drawable.black_pawn
                        }
                    }
                )

                PieceSelection.SELECTED -> painterResource(
                    when(piece.color){
                        PieceColor.WHITE -> when(piece.piece){
                            Pieces.KING -> R.drawable.white_king_selected
                            Pieces.QUEEN -> R.drawable.white_queen_selected
                            Pieces.BISHOP -> R.drawable.white_bishop_selected
                            Pieces.KNIGHT -> R.drawable.white_knight_selected
                            Pieces.ROOK -> R.drawable.white_rook_selected
                            Pieces.PAWN -> R.drawable.white_pawn_selected
                        }

                        PieceColor.BLACK -> when(piece.piece){
                            Pieces.KING -> R.drawable.black_king_selected
                            Pieces.QUEEN -> R.drawable.black_queen_selected
                            Pieces.BISHOP -> R.drawable.black_bishop_selected
                            Pieces.KNIGHT -> R.drawable.black_knight_selected
                            Pieces.ROOK -> R.drawable.black_rook_selected
                            Pieces.PAWN -> R.drawable.black_pawn_selected
                        }
                    }
                )
                PieceSelection.VULNERABLE -> painterResource(
                    when(piece.color){
                        PieceColor.WHITE -> when(piece.piece){
                            Pieces.KING -> R.drawable.white_king_vulnerable
                            Pieces.QUEEN -> R.drawable.white_queen_vulnerable
                            Pieces.BISHOP -> R.drawable.white_bishop_vulnerable
                            Pieces.KNIGHT -> R.drawable.white_knight_vulnerable
                            Pieces.ROOK -> R.drawable.white_rook_vulnerable
                            Pieces.PAWN -> R.drawable.white_pawn_vulnerable
                        }

                        PieceColor.BLACK -> when(piece.piece){
                            Pieces.KING -> R.drawable.black_king_vulnerable
                            Pieces.QUEEN -> R.drawable.black_queen_vulnerable
                            Pieces.BISHOP -> R.drawable.black_bishop_vulnerable
                            Pieces.KNIGHT -> R.drawable.black_knight_vulnerable
                            Pieces.ROOK -> R.drawable.black_rook_vulnerable
                            Pieces.PAWN -> R.drawable.black_pawn_vulnerable
                        }
                    }
                )
            },
        modifier = Modifier
            .size((BOARD_SIZE / 8).dp)
            .offset(posR, posC)
            .rotate(if (piece.flip) 180f else 0f),
        contentDescription = piece.piece.name,
        contentScale = ContentScale.FillBounds
    )
}

