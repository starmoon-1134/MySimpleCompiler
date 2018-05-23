int total;
float sum;
float max(float a,float b){
	float ret;
	ret	= a;
	if(a>=b){
		ret = a;
	}else{
		ret = b;
	}
	return ret;
}
int main(){
	char a[10];
	float b;
	float d;
	int i;
	char tmpchar;
	i=0;
	printf("input array[10] of char_\n");
	while(i<10){
		scanf("%c",tmpchar);
		a[i] = tmpchar;
		getchar();
		i=i+1;
	}
	i=0;
	printf("before sort_\n");
	while(i<10){
		printf("a[%d] is %c\n", i,a[i]);
		i=i+1;
	}
	int times;
	times=1;
	while(times!=0){
		times=0;
		i=0;
		while(i<10-1){
			if(a[i]>a[i+1]){
				char tmp;
				tmp=a[i];
				a[i]=a[i+1];
				a[i+1]=tmp;
				times = times+1;
			}
			i=i+1;
		}
	}
	printf("after sort_\n");
	i=0;
	while(i<10){
		printf("a[%d] is %c\n", i,a[i]);
		i=i+1;
	}
	
	printf("input float b_");
	scanf("%f",b);
	printf("input float d_");
	scanf("%f",d);
	printf("b is %f\n",b);
	printf("d is %f\n",d);
	float maxnum;
	maxnum = max(b,d);
	printf("maxnum is %f\n",maxnum);
	
	return 0;
}

/* int main(){
	int intA,intB;
	float floatA,floatB;
	printf("input intA  \n");
	scanf("%d",intA);
	printf("input intB  \n");
	scanf("%d",intB);
	printf("input floatA  \n");
	scanf("%f",floatA);
	printf("input floatB  \n");
	scanf("%f",floatB);
	printf("intA+intB=%d\n",intA+intB);
	printf("intA-intB=%d\n",intA-intB);
	printf("intA*intB=%d\n",intA*intB);
	printf("intA/intB=%d\n\n",intA/intB);
	
	printf("floatA+floatB=%f\n",floatA+floatB);
	printf("floatA-floatB=%f\n",floatA-floatB);
	printf("floatA*floatB=%f\n",floatA*floatB);
	printf("floatA/floatB=%f\n\n",floatA/floatB);
	
	printf("intA+floatB=%f\n",intA+floatB);
	printf("floatA*intB=%f\n\n",floatA*intB);
	
	printf("intA=%d\n",intA);
	printf("intB=%d\n",intB);
	printf("floatA=%f\n",floatA);
	printf("floatB=%f\n",floatB);
	
	
	return 0;
} */
