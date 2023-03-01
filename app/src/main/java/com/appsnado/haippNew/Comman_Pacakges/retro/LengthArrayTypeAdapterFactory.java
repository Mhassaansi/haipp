package com.appsnado.haippNew.Comman_Pacakges.retro;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.google.gson.stream.JsonToken.NULL;

public final class LengthArrayTypeAdapterFactory
        implements TypeAdapterFactory {

    // The instance holds no state and can be created as a singleton
    private static final TypeAdapterFactory lengthArrayTypeAdapterFactory = new LengthArrayTypeAdapterFactory();

    private LengthArrayTypeAdapterFactory() {
    }

    // However, the factory method does not let a caller to create an instance itself, and _may_ create it itself if necessary (encapsulation)
   public static TypeAdapterFactory getLengthArrayTypeAdapterFactory() {
        return lengthArrayTypeAdapterFactory;
    }

    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
        // Are we dealing with a java.util.List instance?
        if ( List.class.isAssignableFrom(typeToken.getRawType()) ) {
            // Resolve the list element type if possible
            final Type elementType = getElementType(typeToken.getType());
            // And request Gson for the element type adapter
            final TypeAdapter<?> elementTypeAdapter = gson.getAdapter(TypeToken.get(elementType));
            // Some Java boilerplate regarding generics in order not letting the @SuppressWarnings annotation cover too much
            @SuppressWarnings("unchecked")
            final TypeAdapter<T> castTypeAdapter = (TypeAdapter<T>) new LengthArrayTypeAdapter<>(elementTypeAdapter);
            return castTypeAdapter;
        }
        // Or let Gson pick the next downstream type adapter itself
        return null;
    }

    private static Type getElementType(final Type listType) {
        // The given type is not parameterized?
        if ( !(listType instanceof ParameterizedType) ) {
            // Probably the (de)serialized list is raw being not parameterized
            return Object.class;
        }
        final ParameterizedType parameterizedType = (ParameterizedType) listType;
        // Or just take the first type parameter (java.util.List has one type parameter only)
        return parameterizedType.getActualTypeArguments()[0];
    }

    private static final class LengthArrayTypeAdapter<E>
            extends TypeAdapter<List<E>> {

        // This type adapter is designed to read and write a single element only
        // We'll take care of all elements array ourselves
        private final TypeAdapter<E> elementTypeAdapter;

        private LengthArrayTypeAdapter(final TypeAdapter<E> elementTypeAdapter) {
            this.elementTypeAdapter = elementTypeAdapter;
        }

        @Override
        public List<E> read(final JsonReader in)
                throws IOException {
            // Gson type adapters are supposed to be null-friendly
            if ( in.peek() == NULL ) {
                return null;
            }
            // Consume the array begin token `[`
            in.beginArray();
            // The next value is most likely the array length?
            final int arrayLength = in.nextInt();
            final List<E> list = new ArrayList<>();
            // Read until the array has more elements
            while ( in.hasNext() ) {
                // And let the element type adapter read the array element so push the value to the list
                list.add(elementTypeAdapter.read(in));
            }
            // Consume the array end token `]`
            in.endArray();
            assert arrayLength == list.size();
            return list;
        }

        @Override
        @SuppressWarnings("resource")
        public void write(final JsonWriter out, final List<E> list)
                throws IOException {
            if ( list == null ) {
                // Must be null-friendly always
                out.nullValue();
            } else {
                // Writing the `[` token
                out.beginArray();
                // Writing the list size/length
                out.value(list.size());
                for ( final E element : list ) {
                    // And just write each array element
                    elementTypeAdapter.write(out, element);
                }
                // Finalizing the writing with `]`
                out.endArray();
            }
        }
    }

}