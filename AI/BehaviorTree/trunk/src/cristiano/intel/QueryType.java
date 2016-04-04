package cristiano.intel;

public enum QueryType {
QT_GREATER,
QT_LOWER,
QT_EQUAL_OR_GREATER,
QT_EQUAL_OR_LOWER,
QT_EQUAL,
QT_DIFFERENT,QT_ABS_GREATER,
QT_ABS_LOWER,
QT_ABS_EQUAL_OR_GREATER,
QT_ABS_EQUAL_OR_LOWER,
QT_ABS_EQUAL,
QT_ABS_DIFFERENT;

public static boolean checkQuery(QueryType qt,double v1,double v2){
	   
	switch (qt){
	case QT_GREATER:
		return (v1>v2);
	case QT_LOWER:
		return (v1<v2);
	case QT_EQUAL_OR_GREATER:
		return (v1>=v2);
	case QT_EQUAL_OR_LOWER:
		return (v1<=v2);
	case QT_EQUAL:
		return (v1==v2);
	case QT_DIFFERENT:
		return (v1!=v2);
	case QT_ABS_GREATER:
		return (Math.abs(v1)>Math.abs(v2));
	case QT_ABS_LOWER:
		return (Math.abs(v1)<Math.abs(v2));
	case QT_ABS_EQUAL_OR_GREATER:
		return (Math.abs(v1)>=Math.abs(v2));
	case QT_ABS_EQUAL_OR_LOWER:
		return (Math.abs(v1)<=Math.abs(v2));
	case QT_ABS_EQUAL:
		return (Math.abs(v1)==Math.abs(v2));
	case QT_ABS_DIFFERENT:
		return (Math.abs(v1)!=Math.abs(v2));
		
	}
	
	return false;
}
}
