pipeline {

    agent any

    environment {
        DOCKER_IMAGE = 'android-builder'
        APK_PATH = 'app/build/outputs/apk/debug/app-debug.apk'
    }

    stages {

        stage('Check Environment') {
            steps {
                sh '''
                    echo "===== ENVIRONMENT ====="
                    whoami
                    java -version
                    docker --version
                    adb version
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                    echo "===== BUILD DOCKER IMAGE ====="
                    docker build -t ${DOCKER_IMAGE}:latest .
                '''
            }
        }

        stage('Build Android APK') {
            steps {
                sh '''
                    echo "===== BUILD APK ====="

                    docker run --rm \
                        -v "$WORKSPACE:/workspace" \
                        ${DOCKER_IMAGE}:latest \
                        bash -c "
                            chmod +x gradlew &&
                            ./gradlew clean assembleDebug
                        "
                '''
            }
        }

        stage('Verify APK') {
            steps {
                sh '''
                    echo "===== VERIFY APK ====="

                    if [ ! -f "$WORKSPACE/${APK_PATH}" ]; then
                        echo "ERROR: APK not found"
                        exit 1
                    fi

                    ls -lh "$WORKSPACE/${APK_PATH}"
                '''
            }
        }

        stage('Check Cuttlefish') {
            steps {
                sh '''
                    echo "===== CHECK CUTTLEFISH ====="

                    adb wait-for-device
                    adb devices
                '''
            }
        }

        stage('Install APK') {
            steps {
                sh '''
                    echo "===== INSTALL APK ====="

                    adb install -r "$WORKSPACE/${APK_PATH}"
                '''
            }
        }

        stage('Verify Installation') {
            steps {
                sh '''
                    echo "===== VERIFY INSTALLATION ====="

                    adb shell pm list packages | grep -i todo || true
                '''
            }
        }
    }

    post {

        success {
            echo '================================='
            echo 'BUILD SUCCESSFUL'
            echo 'APK installed on Cuttlefish'
            echo '================================='
        }

        failure {
            echo '================================='
            echo 'BUILD FAILED'
            echo 'Check Jenkins Console Output'
            echo '================================='
        }

        always {
            archiveArtifacts(
                artifacts: 'app/build/outputs/apk/debug/*.apk',
                allowEmptyArchive: true,
                fingerprint: true
            )
        }
    }
}
