package diploma.user.mapper;

public interface Mapper<D, E> {

    default D mapToDto(E entity) {
        return null;
    }

    default E mapToEntity(D dto) {
        return null;
    }
}
