/* int total;
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
	
	/*
	printf("input int total_");
	scanf("%d",total);
	float j;
	j=0.0;
	sum=0.0;
	 while(j<total){
		printf("j is %f_\n",j/10.0);
		if(j/10.0==0){
			j=j+1;
			printf("continue\n");
			continue;
		}
		sum=sum+j;
		printf("sum is %f_\n",sum);
		j=j+1;
		if(sum>=1000){
			break;
		}
	} 
	printf("sum is %f\n",sum);
	*/
	
	/*
	return 0;
} */

int main(){
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
	printf("intA/intB=%d\n",intA/intB);
	printf("floatA+floatB=%f\n",floatA+floatB);
	printf("floatA-floatB=%f\n",floatA-floatB);
	printf("floatA*floatB=%f\n",floatA*floatB);
	printf("floatA/floatB=%f\n",floatA/floatB);
	
	printf("intA+floatB=%f\n",intA+floatB);
	printf("floatA*intB=%f\n",floatA*intB);
	
	printf("intA=%d\n",intA);
	printf("intB=%d\n",intB);
	printf("floatA=%f\n",floatA);
	printf("floatB=%f\n",floatB);
	
	
	return 0;
}



/* 
int aaaaa(int i){
	test(i);
	return 0;
}

int test(int i){
	printf("func_test_   %d\n",i);
	return 0;
}

int main(){
	float fff=3.3;
	int b=2;
	char subc=1,c='e';
	char Carr[5];
	int Iarr[3];
	
	Carr[0] = 'a';
	Iarr[2] = 5;
	
	b=10;
	while(b>0){
		printf("b=%d\n",b);
		b = b-1;
	}
	
	if(b>1){
		printf("b>1\n");
		if(Iarr[2]==5){
			printf("Iarr[2]=5\n");
			}
	}else{
		printf("b<=1\n");
	}
	aaaaa(889);
	
	printf("%c\n",c-subc);
	printf("%d\n",Iarr[b]+1);
	
	printf("%f\n",fff+b);
	printf("%f\n",b+fff);
	printf("%f\n",fff+fff);
	printf("%d\n",b+b);
	
	printf("%f\n",fff-b);
	printf("%f\n",b-fff);
	printf("%f\n",fff-1.5);
	printf("%d\n",b-1);
	printf("%d\n",1-b);
	
	return 0;
}

 */