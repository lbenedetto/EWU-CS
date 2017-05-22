#ifndef HW6_INFONODE_H
#define HW6_INFONODE_H


template<class T, class U, class V>
class InfoNode {
	friend class InfoRepository<T, U, V>;

public:
	InfoNode(T &t, U &u, V &v, InfoNode<T, U, V> *p) : dataOne(t), dataTwo(u), dataThree(v), next(p) {}

private:
	T dataOne;
	U dataTwo;
	V dataThree;
	InfoNode *next;

};


#endif //HW6_INFONODE_H
