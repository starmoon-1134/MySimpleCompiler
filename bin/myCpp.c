
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

