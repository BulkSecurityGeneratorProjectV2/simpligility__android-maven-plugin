package com.simpligility.maven.plugins.android.common.aapt;

import com.simpligility.maven.plugins.android.AndroidSdk;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Class that responsible for building appt commands for packaging resources
 */
public final class Aapt1PackageCommandBuilder
        extends AaptCommandBuilder
        implements AaptGenerateSourcesCommandBuilder, AaptLinkCommandBuilder
{
    public Aapt1PackageCommandBuilder( AndroidSdk androidSdk, Log log )
    {
        super( androidSdk, log );
        commands.add( "package" );
    }

    /**
     * Make the resources ID non constant.
     * <p>
     * This is required to make an R java class
     * that does not contain the final value but is used to make reusable compiled
     * libraries that need to access resources.
     *
     * @return current instance of {@link Aapt1PackageCommandBuilder}
     */
    public Aapt1PackageCommandBuilder makeResourcesNonConstant()
    {
        return makeResourcesNonConstant( true );
    }

    /**
     * Make the resources ID non constant.
     * <p>
     * This is required to make an R java class
     * that does not contain the final value but is used to make reusable compiled
     * libraries that need to access resources.
     *
     * @param make if true make resources ID non constant, otherwise ignore
     * @return current instance of {@link Aapt1PackageCommandBuilder}
     */
    public Aapt1PackageCommandBuilder makeResourcesNonConstant( boolean make )
    {
        if ( make )
        {
            log.debug( "Adding non-constant-id" );
            commands.add( "--non-constant-id" );
        }
        return this;
    }

    /**
     * Make package directories under location specified by {@link #setResourceConstantsFolder}.
     *
     * @return current instance of {@link AaptCommandBuilder}
     */
    public Aapt1PackageCommandBuilder makePackageDirectories()
    {
        commands.add( "-m" );
        return this;
    }

    /**
     * Specify where the R java resource constant definitions should be generated or found.
     *
     * @param path path to resource constants folder.
     * @return current instance of {@link AaptCommandBuilder}
     */
    public Aapt1PackageCommandBuilder setResourceConstantsFolder( File path )
    {
        commands.add( "-J" );
        commands.add( path.getAbsolutePath() );
        return this;
    }

    /**
     * Generates R java into a different package.
     *
     * @param packageName package name which generate R.java into
     * @return current instance of {@link AaptCommandBuilder}
     */
    public Aapt1PackageCommandBuilder generateRIntoPackage( String packageName )
    {
        if ( StringUtils.isNotBlank( packageName ) )
        {
            commands.add( "--custom-package" );
            commands.add( packageName );
        }
        return this;
    }

    /**
     * Specify full path to AndroidManifest.xml to include in zip.
     *
     * @param path Path to AndroidManifest.xml
     * @return current instance of {@link AaptCommandBuilder}
     */
    public Aapt1PackageCommandBuilder setPathToAndroidManifest( File path )
    {
        commands.add( "-M" );
        commands.add( path.getAbsolutePath() );
        return this;
    }

    /**
     * Directory in which to find resources.
     * <p>
     * Multiple directories will be scanned and the first match found (left to right) will take precedence.
     *
     * @param resourceDirectory resource directory {@link File}
     * @return current instance of {@link AaptCommandBuilder}
     */
    public Aapt1PackageCommandBuilder addResourceDirectoryIfExists( File resourceDirectory )
    {
        if ( resourceDirectory != null && resourceDirectory.exists() )
        {
            commands.add( "-S" );
            commands.add( resourceDirectory.getAbsolutePath() );
        }
        return this;
    }

    /**
     * Directories in which to find resources.
     * <p>
     * Multiple directories will be scanned and the first match found (left to right) will take precedence.
     *
     * @param resourceDirectories {@link List} of resource directories {@link File}
     * @return current instance of {@link AaptCommandBuilder}
     */
    public Aapt1PackageCommandBuilder addResourceDirectoriesIfExists( List<File> resourceDirectories )
    {
        if ( resourceDirectories != null )
        {
            for ( File resourceDirectory : resourceDirectories )
            {
                addResourceDirectoryIfExists( resourceDirectory );
            }
        }
        return this;
    }

    /**
     * Directories in which to find resources.
     * <p>
     * Multiple directories will be scanned and the first match found (left to right) will take precedence.
     *
     * @param resourceDirectories array of resource directories {@link File}
     * @return current instance of {@link AaptCommandBuilder}
     */
    public Aapt1PackageCommandBuilder addResourceDirectoriesIfExists( File[] resourceDirectories )
    {
        if ( resourceDirectories != null )
        {
            for ( File resourceDirectory : resourceDirectories )
            {
                addResourceDirectoryIfExists( resourceDirectory );
            }
        }
        return this;
    }

    /**
     * Automatically add resources that are only in overlays.
     *
     * @return current instance of {@link AaptCommandBuilder}
     */
    public Aapt1PackageCommandBuilder autoAddOverlay()
    {
        commands.add( "--auto-add-overlay" );
        return this;
    }

    /**
     * Additional directory in which to find raw asset files.
     *
     * @param assetsFolder Folder containing the combined raw assets to add.
     * @return current instance of {@link AaptCommandBuilder}
     */
    public Aapt1PackageCommandBuilder addRawAssetsDirectoryIfExists( File assetsFolder )
    {
        if ( assetsFolder != null && assetsFolder.exists() )
        {
            log.debug( "Adding assets folder : " + assetsFolder );
            commands.add( "-A" );
            commands.add( assetsFolder.getAbsolutePath() );
        }
        return this;
    }

    /**
     * Add an existing package to base include set.
     *
     * @param path Path to existing package to add.
     * @return current instance of {@link AaptCommandBuilder}
     */
    public Aapt1PackageCommandBuilder addExistingPackageToBaseIncludeSet( File path )
    {
        commands.add( "-I" );
        commands.add( path.getAbsolutePath() );
        return this;
    }

    /**
     * Specify which configurations to include.
     * <p>
     * The default is all configurations. The value of the parameter should be a comma
     * separated list of configuration values.  Locales should be specified
     * as either a language or language-region pair.
     *
     * <p>Some examples:<ul>
     * <li>en</li>
     * <li>port,en</li>
     * <li>port,land,en_US</li></ul>
     *
     * <p>If you put the special locale, zz_ZZ on the list, it will perform
     * pseudolocalization on the default locale, modifying all of the
     * strings so you can look for strings that missed the
     * internationalization process.
     * <p>For example:<ul>
     * <li>port,land,zz_ZZ </li></ul>
     *
     * @param configurations configuration to include in form of {@link String}
     * @return current instance of {@link AaptCommandBuilder}
     */
    public Aapt1PackageCommandBuilder addConfigurations( String configurations )
    {
        if ( StringUtils.isNotBlank( configurations ) )
        {
            commands.add( "-c" );
            commands.add( configurations );
        }
        return this;
    }

    /**
     * Adds some additional aapt arguments that are not represented as separate parameters
     * android-maven-plugin configuration.
     *
     * @param extraArguments Array of extra arguments to pass to Aapt.
     * @return current instance of {@link AaptCommandBuilder}
     */
    public Aapt1PackageCommandBuilder addExtraArguments( String[] extraArguments )
    {
        if ( extraArguments != null )
        {
            commands.addAll( Arrays.asList( extraArguments ) );
        }
        return this;
    }

    /**
     * Makes output verbose.
     *
     * @param isVerbose if true aapt will be verbose, otherwise - no
     * @return current instance of {@link AaptCommandBuilder}
     */
    public Aapt1PackageCommandBuilder setVerbose( boolean isVerbose )
    {
        if ( isVerbose )
        {
            commands.add( "-v" );
        }
        return this;
    }

    /**
     * Generates a text file containing the resource symbols of the R class in the
     * specified folder.
     *
     * @param folderForR folder in which text file will be generated
     * @return current instance of {@link AaptCommandBuilder}
     */
    public Aapt1PackageCommandBuilder generateRTextFile( File folderForR )
    {
        commands.add( "--output-text-symbols" );
        commands.add( folderForR.getAbsolutePath() );
        return this;
    }

    /**
     * Force overwrite of existing files.
     *
     * @return current instance of {@link AaptCommandBuilder}
     */
    public Aapt1PackageCommandBuilder forceOverwriteExistingFiles()
    {
        commands.add( "-f" );
        return this;
    }

    /**
     * Disable PNG crunching.
     *
     * @return current instance of {@link AaptCommandBuilder}
     */
    public Aapt1PackageCommandBuilder disablePngCrunching()
    {
        commands.add( "--no-crunch" );
        return this;
    }

    /**
     * Specify the apk file to output.
     *
     * @return current instance of {@link AaptCommandBuilder}
     */
    public Aapt1PackageCommandBuilder setOutputApkFile( File outputFile )
    {
        commands.add( "-F" );
        commands.add( outputFile.getAbsolutePath() );
        return this;
    }

    /**
     * Output Proguard options to a File.
     *
     * @return current instance of {@link AaptCommandBuilder}
     */
    public Aapt1PackageCommandBuilder setProguardOptionsOutputFile( File outputFile )
    {
        if ( outputFile != null )
        {
            final File parentFolder = outputFile.getParentFile();
            if ( parentFolder != null )
            {
                parentFolder.mkdirs();
            }
            log.debug( "Adding proguard file : " + outputFile );
            commands.add( "-G" );
            commands.add( outputFile.getAbsolutePath() );
        }
        return this;
    }

    /**
     * Rewrite the manifest so that its package name is the package name given here. <br>
     * Relative class names (for example .Foo) will be changed to absolute names with the old package
     * so that the code does not need to change.
     *
     * @param manifestPackage new manifest package to apply
     * @return current instance of {@link Aapt1PackageCommandBuilder}
     */
    public Aapt1PackageCommandBuilder renameManifestPackage( String manifestPackage )
    {
        if ( StringUtils.isNotBlank( manifestPackage ) )
        {
            commands.add( "--rename-manifest-package" );
            commands.add( manifestPackage );
        }
        return this;
    }

    /**
     * Rewrite the manifest so that all of its instrumentation components target the given package. <br>
     * Useful when used in conjunction with --rename-manifest-package to fix tests against
     * a package that has been renamed.
     *
     * @param instrumentationPackage new instrumentation target package to apply
     * @return current instance of {@link Aapt1PackageCommandBuilder}
     */
    public Aapt1PackageCommandBuilder renameInstrumentationTargetPackage( String instrumentationPackage )
    {
        if ( StringUtils.isNotBlank( instrumentationPackage ) )
        {
            commands.add( "--rename-instrumentation-target-package" );
            commands.add( instrumentationPackage );
        }
        return this;
    }

    /**
     * Inserts android:debuggable="true" into the application node of the
     * manifest, making the application debuggable even on production devices.
     *
     * @return current instance of {@link Aapt1PackageCommandBuilder}
     */
    public Aapt1PackageCommandBuilder setDebugMode( boolean isDebugMode )
    {
        if ( isDebugMode )
        {
            log.info( "Generating debug apk." );
            commands.add( "--debug-mode" );
        }
        else
        {
            log.info( "Generating release apk." );
        }
        return this;
    }
}
