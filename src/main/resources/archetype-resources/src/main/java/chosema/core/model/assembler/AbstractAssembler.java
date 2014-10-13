package chosema.core.model.assembler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chosema.core.model.HasId;

/**
 * Abstract assembler for conversion between dto objects and models (entities)
 *
 * @param <D>
 * @param <M>
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractAssembler<D extends HasId, M> {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAssembler.class);

	private Class<M> modelTypeClass;
	private Class<D> dtoTypeClass;

	@SuppressWarnings("unchecked")
	public AbstractAssembler() {
		final Type genericSuperclass = getClass().getGenericSuperclass();
		if (genericSuperclass instanceof ParameterizedType) {
			dtoTypeClass = (Class<D>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
			modelTypeClass = (Class<M>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[1];
		}
	}

	public final D convertToDto(final M model) {
		return convertToDto(model, new Object[0]);
	}

	public final D convertToDto(final M model, final Object... args) {
		if (model == null) {
			return null;
		}
		try {
			return convertToDto(model, dtoTypeClass.newInstance(), args);
		} catch (final Exception e) {
			final IllegalArgumentException iae = new IllegalArgumentException(e);
			LOGGER.error("Conversion to dto object failed", iae);
			throw iae;
		}
	}

	protected abstract D convertToDto(M model, D dto, final Object... args);

	public final M convertToModel(final D dto) {
		return convertToModel(dto, new Object[0]);
	}

	public final M convertToModel(final D dto, final Object... args) {
		if (dto == null) {
			return null;
		}
		try {
			final M model = loadOrCreate(dto);
			return convertToModel(model, dto, args);
		} catch (final Exception e) {
			final IllegalArgumentException iae = new IllegalArgumentException(e);
			LOGGER.error("Conversion to model (entity) object failed", iae);
			throw iae;
		}
	}

	protected abstract M convertToModel(M model, D dto, Object... args);

	private M loadOrCreate(final D dto) throws InstantiationException, IllegalAccessException {
		M model = null;
		if (dto.getId() == null) {
			model = modelTypeClass.newInstance();
		} else {
			model = getEntityManager().find(modelTypeClass, dto.getId());
		}
		return model;
	}

	public List<D> toDtos(final List<M> models) {
		if (models == null || models.isEmpty()) {
			return new ArrayList<D>(0);
		} else {
			final List<D> dtos = new ArrayList<D>();
			for (final M model : models) {
				dtos.add(convertToDto(model));
			}
			return dtos;
		}
	}

	public List<M> toModels(final List<D> dtos) {
		if (dtos == null || dtos.isEmpty()) {
			return new ArrayList<M>(0);
		} else {
			final List<M> models = new ArrayList<M>(dtos.size());
			for (final D dto : dtos) {
				models.add(convertToModel(dto));
			}
			return models;
		}
	}

	protected abstract EntityManager getEntityManager();

}
