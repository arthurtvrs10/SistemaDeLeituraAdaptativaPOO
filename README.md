Terminal 1: cd backend && ./mvnw spring-boot:run

Terminal 2: cd frontend/my-app && npm run dev

2. Construir a Imagem (Build)
Execute o comando abaixo para criar a imagem Docker do seu aplicativo Next.js:

// docker build -t leitor-adaptativo-frontend .

3. Executar o Container
Após a conclusão do build, inicie o container mapeando a porta:

// docker run -p 3000:3000 leitor-adaptativo-frontend