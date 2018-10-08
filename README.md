# opentools

## deploy
mvn clean deploy -Darguments=-Dgpg.passphrase="SEE KEEPASS" -Dpgp.skip-true

GPG
    <!-- GPG plugin -->
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-gpg-plugin</artifactId>
        <version>${plugin-gpg.version}</version>
        <executions>
            <execution>
                <id>sign-artifacts</id>
                <phase>verify</phase>
                <goals>
                    <goal>sign</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
mvn clean deploy -Darguments=-Dgpg.passphrase="SEE KEEPASS" -Dpgp.skip-true


Nexus
    <!-- sonatype repo -->
    <plugin>
        <groupId>org.sonatype.plugins</groupId>
        <artifactId>nexus-staging-maven-plugin</artifactId>
        <version>${plugin-nexus-staging.version}</version>
        <extensions>true</extensions>
        <configuration>
            <serverId>ossrh</serverId>
            <nexusUrl>https://oss.sonatype.org/</nexusUrl>
            <!-- Set this to true and the release will automatically proceed and sync to Central Repository will follow  -->
            <autoReleaseAfterClose>false</autoReleaseAfterClose>
        </configuration>
    </plugin>