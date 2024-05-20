package diploma.playlist.controller.advice;

import diploma.playlist.controller.PlaylistController;
import diploma.playlist.exception.IncorrectPlaylistUrlException;
import diploma.playlist.exception.PlaylistAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {PlaylistController.class})
public class PlaylistControllerAdvice {


    @ExceptionHandler(PlaylistAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handlePlaylistAlreadyExist(PlaylistAlreadyExistException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IncorrectPlaylistUrlException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIncorrectPlaylistUrl(IncorrectPlaylistUrlException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
