package domain;

import java.util.List;

public class ExtendedIteratorImplementation<E> implements ExtendedIterator{
	List<E> cityList;
	
	int position = 0;
	
	public ExtendedIteratorImplementation(List<E> list) {
		this.cityList = list;
	}

	@Override
	public boolean hasNext() {
		return position<cityList.size();
	}

	@Override
	public E next() {
		E s = cityList.get(position);
		position++;
		return s;
	}

	@Override
	public E previous() {
		E s = cityList.get(position);
		position--;
		return s;
	}

	@Override
	public boolean hasPrevious() {
		return position>=0;
	}

	@Override
	public void goFirst() {
		position = 0;
	}

	@Override
	public void goLast() {
		position = cityList.size()-1;
	}
	
}
