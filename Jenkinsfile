pipeline {

    agent any

    environment {
        DOCKER_IMAGE = 'android-builder:latest'
        APK_PATH = 'app/build/outputs/apk/debug/app-debug.apk'
    }

    stages {

        /*
         * 1. Check Jenkins environment
         */
        stage('Check Environment') {
            steps {
                sh '''
                    echo "=============================="
                    echo "CHECK ENVIRONMENT"
                    echo "=============================="

                    echo "User:"
                    whoami

                    echo "Java:"
                    java -version

                    echo "Docker:"
                    docker --version

                    echo "ADB:"
                    adb version

                    echo "Workspace:"
                    pwd

                    echo "Files:"
                    ls -la
                '''
            }
        }


        /*
         * 2. Build Docker image
         */
        stage('Build Docker Image') {
            steps {
                sh '''
                    echo "=============================="
                    echo "BUILD DOCKER IMAGE"
                    echo "=============================="

                    docker build \
                        -t ${DOCKER_IMAGE} \
                        .
                '''
            }
        }


        /*
         * 3. Run JUnit tests inside Docker
         */
        stage('JUnit Unit Tests') {
            steps {
                sh '''
                    echo "=============================="
                    echo "RUN JUNIT TESTS"
                    echo "=============================="

                    docker run --rm \
                        -v "$WORKSPACE:/workspace" \
                        ${DOCKER_IMAGE} \
                        bash -c "
                            chmod +x gradlew &&
                            ./gradlew test
                        "
                '''
            }
        }


        /*
         * 4. Build Android APK inside Docker
         */
        stage('Build Android APK') {
            steps {
                sh '''
                    echo "=============================="
                    echo "BUILD ANDROID APK"
                    echo "=============================="

                    docker run --rm \
                        -v "$WORKSPACE:/workspace" \
                        ${DOCKER_IMAGE} \
                        bash -c "
                            chmod +x gradlew &&
                            ./gradlew clean assembleDebug
                        "
                '''
            }
        }


        /*
         * 5. Verify APK
         */
        stage('Verify APK') {
            steps {
                sh '''
                    echo "=============================="
                    echo "VERIFY APK"
                    echo "=============================="

                    if [ ! -f "$WORKSPACE/${APK_PATH}" ]; then
                        echo "ERROR: APK NOT FOUND"
                        exit 1
                    fi

                    echo "APK found:"
                    ls -lh "$WORKSPACE/${APK_PATH}"
                '''
            }
        }


        /*
         * 6. Check Cuttlefish
         */
        stage('Check Cuttlefish') {
            steps {
                sh '''
                    echo "=============================="
                    echo "CHECK CUTTLEFISH"
                    echo "=============================="

                    adb start-server

                    adb wait-for-device

                    echo "Connected devices:"
                    adb devices
                '''
            }
        }


        /*
         * 7. Install APK on Cuttlefish
         */
        stage('Install APK on Cuttlefish') {
            steps {
                sh '''
                    echo "=============================="
                    echo "INSTALL APK"
                    echo "=============================="

                    adb install -r "$WORKSPACE/${APK_PATH}"
                '''
            }
        }


        /*
         * 8. Verify application installation
         */
        stage('Verify Installation') {
            steps {
                sh '''
                    echo "=============================="
                    echo "VERIFY INSTALLATION"
                    echo "=============================="

                    adb shell pm list packages | grep "com.shivprakash.to_dolist" || {
                        echo "Application package not found"
                        exit 1
                    }

                    echo "Application successfully installed."
                '''
            }
        }
    }


    /*
     * Actions after pipeline
     */
    post {

        /*
         * Always publish JUnit results
         */
        always {

            echo "=============================="
            echo "PUBLISH JUNIT RESULTS"
            echo "=============================="

            junit(
                testResults: '**/build/test-results/**/TEST-*.xml',
                allowEmptyResults: true
            )


            echo "=============================="
            echo "ARCHIVE APK"
            echo "=============================="

            archiveArtifacts(
                artifacts: 'app/build/outputs/apk/debug/*.apk',
                allowEmptyArchive: true,
                fingerprint: true
            )
        }


        /*
         * Pipeline successful
         */
        success {
            echo """
            =====================================
            PIPELINE SUCCESSFUL
            =====================================

            JUnit Tests       : PASSED
            Android Build     : PASSED
            APK               : GENERATED
            Cuttlefish        : CONNECTED
            APK Installation  : SUCCESS

            =====================================
            """
        }


        /*
         * Pipeline failed
         */
        failure {
            echo """
            =====================================
            PIPELINE FAILED
            =====================================

            Check the Jenkins Console Output.

            =====================================
            """
        }
    }
}
