package db;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.*;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DynamoDbTestContainersTest {

    static final DockerImageName IMAGE = DockerImageName.parse("localstack/localstack:3.0");

    static final LocalStackContainer localstack = new LocalStackContainer(IMAGE)
            .withServices(LocalStackContainer.Service.DYNAMODB);

    private DynamoDbClient client;

    @BeforeAll
    void setup() {
        localstack.start();

        client = DynamoDbClient.builder()
                .region(Region.of(localstack.getRegion()))
                .endpointOverride(localstack.getEndpointOverride(LocalStackContainer.Service.DYNAMODB))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("test", "test")))
                .build();

        // 테이블 생성
        client.createTable(CreateTableRequest.builder()
                .tableName("MyTable")
                .keySchema(KeySchemaElement.builder()
                        .attributeName("id")
                        .keyType(KeyType.HASH)
                        .build())
                .attributeDefinitions(AttributeDefinition.builder()
                        .attributeName("id")
                        .attributeType(ScalarAttributeType.S)
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .build());
    }

    @AfterAll
    void tearDown() {
        localstack.stop();
    }

    @Test
    void testPutAndGetItem() {
        // 데이터 저장
        client.putItem(PutItemRequest.builder()
                .tableName("MyTable")
                .item(Map.of(
                        "id", AttributeValue.fromS("123"),
                        "name", AttributeValue.fromS("test-user")
                ))
                .build());

        // 데이터 조회
        Map<String, AttributeValue> item = client.getItem(GetItemRequest.builder()
                .tableName("MyTable")
                .key(Map.of("id", AttributeValue.fromS("123")))
                .build()).item();

        // 검증
        assertThat(item).isNotNull();
        assertThat(item.get("name").s()).isEqualTo("test-user");
    }
}
