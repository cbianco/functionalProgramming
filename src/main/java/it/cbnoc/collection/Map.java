/*
package it.cbnoc.collection;

import it.cbnoc.Tuple2.Tuple2;
import it.cbnoc.Tuple2.Tuple2;
import it.cbnoc.tuple.Tuple2;
import it.cbnoc.utils.Result;

import static it.cbnoc.collection.MapEntry.mapEntry;

public class Map<K extends Comparable<K>, V> {

	protected final Tree<MapEntry<Integer, List<Tuple2<K, V>>>> delegate;

	private Map() {
		this.delegate = Tree.empty();
	}
	public Map(Tree<MapEntry<Integer, List<Tuple2<K, V>>>> delegate) {
		this.delegate = delegate;
	}

	public Map<K, V> add(K key, V value) {
		Tuple2<K, V> Tuple2 = new Tuple2<>(key, value);
		List<Tuple2<K, V>> ltkv = getAll(key).map(lt ->
			lt.foldLeft(List.list(Tuple2), l -> t -> t._1.equals(key)
				? l
				: l.cons(t))).getOrElse(() -> List.list(Tuple2));
		return new Map<>(delegate.insert(mapEntry(key.hashCode(), ltkv)));
	}

	public boolean contains(K key) {
		return getAll(key).map(lt -> lt.exists(t ->
			t._1.equals(key))).getOrElse(false);
	}

	public MapEntry<K, V> max() {
		return delegate.max();
	}

	public MapEntry<K, V> min() {
		return delegate.min();
	}

	public Map<K, V> remove(K key) {
		List<Tuple2<K, V>> ltkv = getAll(key).map(lt ->
			lt.foldLeft(List.<Tuple2<K, V>>list(), l -> t -> t._1.equals(key)
				? l
				: l.cons(t))).getOrElse(List::list);
		return ltkv.isEmpty()
			? new Map<>(delegate.delete(MapEntry.mapEntry(key.hashCode())))
			: new Map<>(delegate.insert(mapEntry(key.hashCode(), ltkv)));
	}

	public Result<Tuple2<K, V>> get(K key) {
		return getAll(key).flatMap(lt -> lt.first(t -> t._1.equals(key)));
	}

	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	public static <K extends Comparable<K>, V> Map<K, V> empty() {
		return new Map<>();
	}

	public List<V> values() {
		return List.sequence(delegate.foldInReverseOrder(List.<Result<V>>list(),
			lst1 -> me -> lst2 -> List.concat(lst2,
				lst1.cons(me.value)))).getOrElse(List.list());
	}

	private Result<List<Tuple2<K, V>>> getAll(K key) {
		return delegate.get(mapEntry(key.hashCode()))
			.flatMap(x -> x.value.map(lt -> lt.map(t -> t)));
	}
}
*/
